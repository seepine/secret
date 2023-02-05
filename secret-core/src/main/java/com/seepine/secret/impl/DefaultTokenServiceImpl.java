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
import com.seepine.tool.Run;
import com.seepine.tool.util.Objects;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author seepine
 * @since 0.0.6
 */
public class DefaultTokenServiceImpl implements TokenService {
  private final Algorithm algorithm;
  private final AuthProperties authProperties;
  private final JWTVerifier verifier;
  private static final String BLACK_PRE = "secret_blacklist:";
  private static final String WHITE_PRE = "secret_whitelist:";
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
      Run.nonEmpty(authUser.getId(), val -> jwtBuilder.withClaim("id", val));
      Run.nonEmpty(authUser.getNickName(), val -> jwtBuilder.withClaim("nickName", val));
      Run.nonEmpty(authUser.getFullName(), val -> jwtBuilder.withClaim("fullName", val));
      Run.nonEmpty(authUser.getPhone(), val -> jwtBuilder.withClaim("phone", val));
      Run.nonEmpty(authUser.getEmail(), val -> jwtBuilder.withClaim("email", val));
      Run.nonEmpty(authUser.getAvatarUrl(), val -> jwtBuilder.withClaim("avatarUrl", val));
      Run.nonEmpty(authUser.getSignTime(), val -> jwtBuilder.withClaim("signTime", val));
      Run.nonEmpty(authUser.getRefreshTime(), val -> jwtBuilder.withClaim("refreshTime", val));
      Run.nonEmpty(authUser.getTenantName(), val -> jwtBuilder.withClaim("tenantName", val));
      Run.nonEmpty(authUser.getTenantId(), val -> jwtBuilder.withClaim("tenantId", val));
      try {
        Run.nonEmpty(authUser.getClaims(), val -> jwtBuilder.withClaim("claims", val));
      } catch (IllegalArgumentException ignore) {
        throw new IllegalArgumentException(
            "Expected authUser claims containing Boolean, Integer, Long, Double, String and Date. Your claims: "
                + authUser.getClaims().toString());
      }
      String token = jwtBuilder.sign(algorithm);
      // 单独设置permissions到缓存，避免写入到jwt请求头过长
      AuthUtil.getCacheService()
          .setCache(
              authProperties.getCachePrefix() + PERMISSION_PRE + token,
              authUser.getPermissions(),
              authProperties.getExpiresAt() > 0 ? authProperties.getExpiresAt() * 1000 : 0);
      // 如果开启白名单，保存token
      if (authProperties.getEnableWhitelist()) {
        AuthUtil.getCacheService()
            .setCache(
                authProperties.getCachePrefix() + WHITE_PRE + token,
                token,
                authProperties.getExpiresAt() > 0 ? authProperties.getExpiresAt() * 1000 : 0);
      }
      return token;
    } catch (JWTCreationException exception) {
      throw new AuthException(AuthExceptionType.GENERATE_TOKEN_FAIL);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public AuthUser analyze(String token) throws AuthException {
    try {
      if (AuthUtil.getCacheService().getCache(authProperties.getCachePrefix() + BLACK_PRE + token)
          != null) {
        throw new AuthException(AuthExceptionType.INVALID_TOKEN);
      }
      // 如果开启白名单，需要判断token是否存在
      if (authProperties.getEnableWhitelist()) {
        if (AuthUtil.getCacheService().getCache(authProperties.getCachePrefix() + WHITE_PRE + token)
            == null) {
          throw new AuthException(AuthExceptionType.INVALID_TOKEN);
        }
      }
      DecodedJWT decodedJwt = verifier.verify(token);
      Object permissions =
          AuthUtil.getCacheService()
              .getCache(authProperties.getCachePrefix() + PERMISSION_PRE + token);
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
          .permissions(permissions != null ? (Set<String>) permissions : new HashSet<>(16))
          .claims(Objects.require(decodedJwt.getClaim("claims").asMap(), new HashMap<>(16)))
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
    // 如果开启白名单，退出登录时需清除
    if (authProperties.getEnableWhitelist()) {
      AuthUtil.getCacheService().remove(authProperties.getCachePrefix() + WHITE_PRE + token);
    }
    AuthUtil.getCacheService()
        .setCache(
            BLACK_PRE + token,
            true,
            authProperties.getExpiresAt() > 0 ? authProperties.getExpiresAt() * 1000 : 0);
  }
}
