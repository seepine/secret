package com.seepine.secret.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.seepine.secret.AuthUtil;
import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.exception.ExpiresSecretException;
import com.seepine.secret.exception.SecretException;
import com.seepine.secret.exception.TokenSecretException;
import com.seepine.secret.interfaces.TokenService;
import com.seepine.secret.properties.AuthProperties;
import com.seepine.tool.Run;
import com.seepine.tool.cache.Cache;
import com.seepine.tool.time.CurrentTimeMillis;
import com.seepine.tool.util.Objects;
import com.seepine.tool.util.Strings;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
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
  public AuthUser generate(@Nonnull AuthUser authUser) throws SecretException {
    try {
      Instant now = Instant.ofEpochSecond(authUser.getSignAt());
      JWTCreator.Builder jwtBuilder =
          JWT.create().withIssuer(authProperties.getIssuer()).withIssuedAt(now);
      jwtBuilder.withExpiresAt(Instant.ofEpochSecond(authUser.getExpiresAt()));
      Run.nonNull(authUser.getId(), val -> jwtBuilder.withClaim("id", val));
      Run.nonNull(authUser.getNickName(), val -> jwtBuilder.withClaim("nickName", val));
      Run.nonNull(authUser.getFullName(), val -> jwtBuilder.withClaim("fullName", val));
      Run.nonNull(authUser.getUsername(), val -> jwtBuilder.withClaim("username", val));
      Run.nonNull(authUser.getPhone(), val -> jwtBuilder.withClaim("phone", val));
      Run.nonNull(authUser.getEmail(), val -> jwtBuilder.withClaim("email", val));
      Run.nonNull(authUser.getAvatarUrl(), val -> jwtBuilder.withClaim("avatarUrl", val));
      Run.nonNull(authUser.getSignAt(), val -> jwtBuilder.withClaim("signAt", val));
      Run.nonNull(authUser.getRefreshAt(), val -> jwtBuilder.withClaim("refreshAt", val));
      Run.nonNull(authUser.getTenantName(), val -> jwtBuilder.withClaim("tenantName", val));
      Run.nonNull(authUser.getTenantId(), val -> jwtBuilder.withClaim("tenantId", val));
      Run.nonNull(authUser.getPlatform(), val -> jwtBuilder.withClaim("platform", val));
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
      authUser.setToken(jwtBuilder.sign(algorithm));
      // 保存 permissions 在缓存里
      Run.nonEmpty(
          authUser.getPermissions(),
          permissions ->
              Cache.set(
                  getKey(authUser.getId()) + ":permissions",
                  permissions,
                  (authUser.getExpiresAt() - authUser.getRefreshAt()) * 1000));
      return authUser;
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
      AuthUser authUser =
          AuthUser.builder()
              .id(decodedJwt.getClaim("id").asString())
              .nickName(decodedJwt.getClaim("nickName").asString())
              .fullName(decodedJwt.getClaim("fullName").asString())
              .username(decodedJwt.getClaim("username").asString())
              .phone(decodedJwt.getClaim("phone").asString())
              .email(decodedJwt.getClaim("email").asString())
              .avatarUrl(decodedJwt.getClaim("avatarUrl").asString())
              .signAt(decodedJwt.getIssuedAt().getTime() / 1000)
              .refreshAt(decodedJwt.getClaim("refreshAt").asLong())
              .expiresAt(
                  Optional.ofNullable(decodedJwt.getExpiresAt())
                      .map(item -> item.getTime() / 1000)
                      .orElse(null))
              .tenantId(decodedJwt.getClaim("tenantId").asString())
              .tenantName(decodedJwt.getClaim("tenantName").asString())
              .platform(decodedJwt.getClaim("platform").asString())
              .roles(roles)
              .claims(Objects.require(decodedJwt.getClaim("claims").asMap(), new HashMap<>(16)))
              .token(token)
              .build();
      authUser.setPermissions(AuthUtil.getPermissionService().query(authUser));
      return authUser;
    } catch (SecretException e) {
      throw e;
    } catch (TokenExpiredException e) {
      throw new ExpiresSecretException(e.getExpiredOn().getEpochSecond());
    } catch (Exception e) {
      throw new TokenSecretException(e);
    }
  }

  /**
   * jwt的刷新token信息，表示重新生成一个新token
   *
   * @param authUser 用户信息
   * @return authUser
   */
  @Nonnull
  @Override
  public AuthUser refresh(@Nonnull AuthUser authUser) throws SecretException {
    return generate(authUser);
  }

  @Override
  public void clear(@Nonnull AuthUser authUser) throws SecretException {
    long expire = authUser.getExpiresAt() - authUser.getRefreshAt();
    if (expire > 0) {
      Cache.set(
          getKey(authUser.getToken()) + ":logout",
          String.valueOf(CurrentTimeMillis.now() / 1000),
          expire * 1000);
    }
  }

  private String getKey(String token) {
    return authProperties.getCachePrefix() + Strings.COLON + token;
  }
}
