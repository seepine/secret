package com.seepine.secret.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.IncorrectClaimException;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.core.type.TypeReference;
import com.seepine.json.Json;
import com.seepine.json.JsonObject;
import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.enums.AuthExceptionType;
import com.seepine.secret.exception.AuthException;
import com.seepine.secret.interfaces.AuthService;
import com.seepine.secret.properties.AuthProperties;
import com.seepine.secret.util.CacheUtil;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * @author seepine
 * @since 0.0.6
 */
public class DefaultAuthServiceImpl implements AuthService {
  private final Algorithm algorithm;
  private final AuthProperties authProperties;
  private final JWTVerifier verifier;

  public DefaultAuthServiceImpl(AuthProperties authProperties) {
    this.authProperties = authProperties;
    this.algorithm = Algorithm.HMAC256(authProperties.getSecret());
    this.verifier = JWT.require(algorithm).withIssuer(authProperties.getIssuer()).build();
  }

  @Override
  public <T extends AuthUser> String genToken(T authUser) {
    try {
      Instant now = Instant.now();
      JWTCreator.Builder jwtBuilder =
          JWT.create().withIssuer(authProperties.getIssuer()).withIssuedAt(now);
      if (authProperties.getExpiresAt() > 0) {
        jwtBuilder.withExpiresAt(now.plusSeconds(authProperties.getExpiresAt()));
      }
      Map<String, Object> map = Json.convert(authUser, new TypeReference<>() {});
      map.forEach(
          (key, value) -> {
            if (value != null && !"permissions".equals(key)) {
              if (value instanceof String) {
                jwtBuilder.withClaim(key, (String) value);
              } else if (value instanceof Long) {
                jwtBuilder.withClaim(key, (Long) value);
              } else if (value instanceof Integer) {
                jwtBuilder.withClaim(key, (Integer) value);
              } else if (value instanceof Boolean) {
                jwtBuilder.withClaim(key, (Boolean) value);
              } else if (value instanceof Double) {
                jwtBuilder.withClaim(key, (Double) value);
              } else if (value instanceof Date) {
                jwtBuilder.withClaim(key, (Date) value);
              } else {
                jwtBuilder.withClaim(key, value.toString());
              }
            }
          });
      String token = jwtBuilder.sign(algorithm);
      CacheUtil.set(
          authProperties.getCachePrefix() + token,
          authUser.getPermissions(),
          authProperties.getExpiresAt() > 0 ? authProperties.getExpiresAt() * 1000 : 0);
      return token;
    } catch (JWTCreationException exception) {
      throw new AuthException(AuthExceptionType.GEN_TOKEN_FAIL);
    }
  }

  @Override
  public <T extends AuthUser> T get(String token) throws AuthException {
    try {
      if (CacheUtil.get("secret_blacklist:" + token) != null) {
        throw new AuthException(AuthExceptionType.INVALID_TOKEN);
      }
      DecodedJWT decodedJWT = verifier.verify(token);
      JsonObject json = new JsonObject();
      decodedJWT
          .getClaims()
          .forEach(
              (key, claim) -> {
                if (!claim.isMissing() && !claim.isNull()) {
                  Object val = getValue(claim);
                  if (val != null) {
                    json.set(key, val);
                  }
                }
              });
      T authUser = Json.parse(json.toString(), new TypeReference<>() {});
      authUser.setPermissions(CacheUtil.get(authProperties.getCachePrefix() + token));
      authUser.setToken(token);
      return authUser;
    } catch (IncorrectClaimException e) {
      throw new AuthException(AuthExceptionType.INCORRECT_ISSUER);
    } catch (TokenExpiredException e) {
      throw new AuthException(AuthExceptionType.TOKEN_EXPIRES);
    } catch (Exception e) {
      throw new AuthException(AuthExceptionType.VERIFY_TOKEN_FAIL);
    }
  }

  private Object getValue(Claim claim) {
    String val = claim.asString();
    if (val != null) {
      return val;
    }
    Long valLong = claim.asLong();
    if (valLong != null) {
      return valLong;
    }
    Integer valInt = claim.asInt();
    if (valInt != null) {
      return valInt;
    }
    Double valDouble = claim.asDouble();
    if (valDouble != null) {
      return valDouble;
    }
    Boolean valBool = claim.asBoolean();
    if (valBool != null) {
      return valBool;
    }
    return claim.asDate();
  }

  @Override
  public void remove(String token) {
    CacheUtil.remove(authProperties.getCachePrefix() + token);
    CacheUtil.set(
        "secret_blacklist:" + token,
        true,
        authProperties.getExpiresAt() > 0 ? authProperties.getExpiresAt() * 1000 : 0);
  }
}
