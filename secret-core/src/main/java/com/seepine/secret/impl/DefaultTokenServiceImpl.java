package com.seepine.secret.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.IncorrectClaimException;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.seepine.secret.AuthUtil;
import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.enums.AuthExceptionType;
import com.seepine.secret.exception.AuthException;
import com.seepine.secret.interfaces.TokenService;
import com.seepine.secret.properties.AuthProperties;
import com.seepine.secret.util.CacheUtil;
import com.seepine.tool.Run;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author seepine
 * @since 0.0.6
 */
public class DefaultTokenServiceImpl implements TokenService {
  private final Algorithm algorithm;
  private final AuthProperties authProperties;
  private final JWTVerifier verifier;
  private static final String BLACK_PRE = "secret_blacklist:";
  private static final String PERMISSION_PRE = "permissions:";

  public DefaultTokenServiceImpl(AuthProperties authProperties) {
    this.authProperties = authProperties;
    this.algorithm = Algorithm.HMAC256(authProperties.getSecret());
    this.verifier = JWT.require(algorithm).withIssuer(authProperties.getIssuer()).build();
  }

  @Override
  public String generate(AuthUser authUser) {
    try {
      Instant now = Instant.now();
      JWTCreator.Builder jwtBuilder =
          JWT.create().withIssuer(authProperties.getIssuer()).withIssuedAt(now);
      if (authProperties.getExpiresAt() > 0) {
        jwtBuilder.withExpiresAt(now.plusSeconds(authProperties.getExpiresAt()));
      }
      Run.notEmpty(authUser.getId(), val -> jwtBuilder.withClaim("id", val));
      Run.notEmpty(authUser.getNickName(), val -> jwtBuilder.withClaim("nickName", val));
      Run.notEmpty(authUser.getFullName(), val -> jwtBuilder.withClaim("fullName", val));
      Run.notEmpty(authUser.getPhone(), val -> jwtBuilder.withClaim("phone", val));
      Run.notEmpty(authUser.getEmail(), val -> jwtBuilder.withClaim("email", val));
      Run.notEmpty(authUser.getAvatarUrl(), val -> jwtBuilder.withClaim("avatarUrl", val));
      Run.notEmpty(authUser.getSignTime(), val -> jwtBuilder.withClaim("signTime", val));
      Run.notEmpty(authUser.getRefreshTime(), val -> jwtBuilder.withClaim("refreshTime", val));
      Run.notEmpty(authUser.getTenantName(), val -> jwtBuilder.withClaim("tenantName", val));
      Run.notEmpty(authUser.getTenantId(), val -> jwtBuilder.withClaim("tenantId", val));
      Run.notEmpty(authUser.getClaims(), val -> jwtBuilder.withClaim("claims", val));
      String token = jwtBuilder.sign(algorithm);
      // 单独设置permissions到缓存，避免写入到jwt请求头过长
      AuthUtil.getCacheService()
          .setCache(
              authProperties.getCachePrefix() + PERMISSION_PRE + token,
              authUser.getPermissions(),
              authProperties.getExpiresAt() > 0 ? authProperties.getExpiresAt() * 1000 : 0);
      return token;
    } catch (JWTCreationException exception) {
      throw new AuthException(AuthExceptionType.GENERATE_TOKEN_FAIL);
    }
  }

  @Override
  public AuthUser analyze(String token) throws AuthException {
    try {
      if (AuthUtil.getCacheService().getCache(BLACK_PRE + token) != null) {
        throw new AuthException(AuthExceptionType.INVALID_TOKEN);
      }
      DecodedJWT decodedJwt = verifier.verify(token);
      return AuthUser.builder()
          .id(decodedJwt.getClaim("id").asString())
          .nickName(decodedJwt.getClaim("nickName").asString())
          .fullName(decodedJwt.getClaim("fullName").asString())
          .phone(decodedJwt.getClaim("phone").asString())
          .email(decodedJwt.getClaim("email").asString())
          .avatarUrl(decodedJwt.getClaim("avatarUrl").asString())
          .signTime(decodedJwt.getClaim("signTime").asLong())
          .refreshTime(decodedJwt.getClaim("refreshTime").asLong())
          .tenantId(decodedJwt.getClaim("tenantId").asString())
          .tenantName(decodedJwt.getClaim("tenantName").asString())
          .permissions(
              Run.require(
                  CacheUtil.get(authProperties.getCachePrefix() + token), new HashSet<>(16)))
          .claims(Run.require(decodedJwt.getClaim("claims").asMap(), new HashMap<>(16)))
          .token(token)
          .build();
    } catch (IncorrectClaimException e) {
      throw new AuthException(AuthExceptionType.INCORRECT_ISSUER);
    } catch (TokenExpiredException e) {
      throw new AuthException(AuthExceptionType.TOKEN_EXPIRES);
    } catch (Exception e) {
      throw new AuthException(AuthExceptionType.ANALYZE_TOKEN_FAIL);
    }
  }

  @Override
  public void clear(String token) {
    AuthUtil.getCacheService().remove(authProperties.getCachePrefix() + token);
    AuthUtil.getCacheService()
        .setCache(
            BLACK_PRE + token,
            true,
            authProperties.getExpiresAt() > 0 ? authProperties.getExpiresAt() * 1000 : 0);
  }
}
