package com.seepine.secret.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.seepine.secret.AuthUtil;
import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.entity.TokenInfo;
import com.seepine.secret.exception.ExpiresSecretException;
import com.seepine.secret.exception.SecretException;
import com.seepine.secret.exception.TokenSecretException;
import com.seepine.secret.interfaces.TokenService;
import com.seepine.secret.properties.AuthProperties;
import com.seepine.tool.Run;
import com.seepine.tool.cache.Cache;
import com.seepine.tool.time.CurrentTime;
import com.seepine.tool.time.CurrentTimeMillis;
import com.seepine.tool.util.Objects;
import com.seepine.tool.util.Strings;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * @author seepine
 * @since 0.0.6
 */
public class DefaultTokenServiceImpl implements TokenService {
  private final AuthProperties authProperties;
  private final Algorithm algorithm;
  private final JWTVerifier verifier;

  public DefaultTokenServiceImpl(AuthProperties authProperties) {
    this.authProperties = authProperties;
    this.algorithm = Algorithm.HMAC256(authProperties.getSecret());
    this.verifier = JWT.require(algorithm).withIssuer(authProperties.getIssuer()).build();
  }

  @Nonnull
  @Override
  public String generate(@Nonnull final AuthUser authUser, @Nonnegative final Long expires)
      throws SecretException {
    try {
      Instant now = Instant.ofEpochSecond(CurrentTime.second());
      JWTCreator.Builder jwtBuilder =
          JWT.create().withIssuer(authProperties.getIssuer()).withIssuedAt(now);
      jwtBuilder.withExpiresAt(Instant.ofEpochSecond(CurrentTime.second() + expires));

      Run.nonNull(authUser.getId(), val -> jwtBuilder.withClaim("id", val));
      Run.nonNull(authUser.getNickName(), val -> jwtBuilder.withClaim("nickName", val));
      Run.nonNull(authUser.getFullName(), val -> jwtBuilder.withClaim("fullName", val));
      Run.nonNull(authUser.getUsername(), val -> jwtBuilder.withClaim("username", val));
      Run.nonNull(authUser.getPhone(), val -> jwtBuilder.withClaim("phone", val));
      Run.nonNull(authUser.getEmail(), val -> jwtBuilder.withClaim("email", val));
      Run.nonNull(authUser.getAvatarUrl(), val -> jwtBuilder.withClaim("avatarUrl", val));
      Run.nonNull(authUser.getTenantName(), val -> jwtBuilder.withClaim("tenantName", val));
      Run.nonNull(authUser.getTenantId(), val -> jwtBuilder.withClaim("tenantId", val));
      Run.nonNull(authUser.getPlatform(), val -> jwtBuilder.withClaim("platform", val));
      Run.nonNull(authUser.getTokenInfo(), val -> jwtBuilder.withClaim("tokenInfo", val.toMap()));
      Run.nonEmpty(
          authUser.getRoles(),
          val -> jwtBuilder.withArrayClaim("roles", val.toArray(new String[] {})));
      try {
        Run.nonEmpty(authUser.getClaims(), val -> jwtBuilder.withClaim("claims", val));
      } catch (IllegalArgumentException ignore) {
        throw new IllegalArgumentException(
            "Expected authUser claims containing Boolean, Integer, Long, Double, String and Date. Your claims: "
                + authUser.getClaims().toString());
      }
      return jwtBuilder.sign(algorithm);
    } catch (JWTCreationException exception) {
      throw new TokenSecretException(exception);
    }
  }

  @Nonnull
  @Override
  public AuthUser analyze(@Nonnull String token) throws SecretException {
    try {
      String expires = Cache.get(getKey(token) + ":logout");
      if (expires != null) {
        throw new ExpiresSecretException(Long.valueOf(expires));
      }
      DecodedJWT decodedJwt = verifier.verify(token);
      // 获取roles
      Set<String> roles = new HashSet<>();
      if (!decodedJwt.getClaim("roles").isMissing() && !decodedJwt.getClaim("roles").isNull()) {
        roles = new HashSet<>(decodedJwt.getClaim("roles").asList(String.class));
      }
      TokenInfo tokenInfo = TokenInfo.parse(decodedJwt.getClaim("tokenInfo").asMap());
      tokenInfo.setAccessToken(token);
      AuthUser authUser =
          AuthUser.builder()
              .id(decodedJwt.getClaim("id").asString())
              .nickName(decodedJwt.getClaim("nickName").asString())
              .fullName(decodedJwt.getClaim("fullName").asString())
              .username(decodedJwt.getClaim("username").asString())
              .phone(decodedJwt.getClaim("phone").asString())
              .email(decodedJwt.getClaim("email").asString())
              .avatarUrl(decodedJwt.getClaim("avatarUrl").asString())
              .tenantId(decodedJwt.getClaim("tenantId").asString())
              .tenantName(decodedJwt.getClaim("tenantName").asString())
              .platform(decodedJwt.getClaim("platform").asString())
              .roles(roles)
              .tokenInfo(tokenInfo)
              .claims(Objects.require(decodedJwt.getClaim("claims").asMap(), new HashMap<>(16)))
              .build();
      authUser.setPermissions(AuthUtil.getPermissionService().query(authUser));
      return authUser;
    } catch (SecretException e) {
      throw e;
    } catch (TokenExpiredException e) {
      throw new ExpiresSecretException(e.getExpiredOn().getEpochSecond());
    } catch (SignatureVerificationException e) {
      throw new TokenSecretException();
    } catch (Exception e) {
      throw new TokenSecretException(e);
    }
  }

  @Override
  public void clear(@Nonnull AuthUser authUser) throws SecretException {
    long expire = authUser.getTokenInfo().getExpires();
    if (expire > 0) {
      Cache.set(
          getKey(authUser.getTokenInfo().getAccessToken()) + ":logout",
          String.valueOf(CurrentTimeMillis.now() / 1000),
          expire * 1000);
    }
  }

  private String getKey(String token) {
    return authProperties.getCachePrefix() + Strings.COLON + token;
  }
}
