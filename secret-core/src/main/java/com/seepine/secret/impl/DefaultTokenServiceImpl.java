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
import com.seepine.secret.exception.ExpiresSecretException;
import com.seepine.secret.exception.SecretException;
import com.seepine.secret.exception.TokenSecretException;
import com.seepine.secret.interfaces.TokenService;
import com.seepine.secret.properties.AuthProperties;
import com.seepine.tool.Run;
import com.seepine.tool.cache.Cache;
import com.seepine.tool.time.CurrentTime;
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
      Run.nonNull(authUser.getName(), val -> jwtBuilder.withClaim("name", val));
      Run.nonNull(authUser.getNickname(), val -> jwtBuilder.withClaim("nickname", val));
      Run.nonNull(authUser.getEmail(), val -> jwtBuilder.withClaim("email", val));
      Run.nonNull(authUser.getEmailVerified(), val -> jwtBuilder.withClaim("emailVerified", val));
      Run.nonNull(authUser.getPhoneNumber(), val -> jwtBuilder.withClaim("phoneNumber", val));
      Run.nonNull(
          authUser.getPhoneNumberVerified(),
          val -> jwtBuilder.withClaim("phoneNumberVerified", val));
      Run.nonNull(authUser.getPicture(), val -> jwtBuilder.withClaim("picture", val));
      Run.nonNull(authUser.getGender(), val -> jwtBuilder.withClaim("gender", val));
      Run.nonNull(authUser.getAddress(), val -> jwtBuilder.withClaim("address", val));
      Run.nonNull(authUser.getTenantId(), val -> jwtBuilder.withClaim("tenantId", val));
      Run.nonNull(authUser.getPlatform(), val -> jwtBuilder.withClaim("platform", val));
      Run.nonNull(authUser.getSignAt(), val -> jwtBuilder.withClaim("signAt", val));
      Run.nonNull(authUser.getRefreshAt(), val -> jwtBuilder.withClaim("refreshAt", val));
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
      AuthUser authUser =
          AuthUser.builder()
              .id(decodedJwt.getClaim("id").asString())
              .name(decodedJwt.getClaim("name").asString())
              .nickname(decodedJwt.getClaim("nickname").asString())
              .email(decodedJwt.getClaim("email").asString())
              .emailVerified(decodedJwt.getClaim("emailVerified").asBoolean())
              .phoneNumber(decodedJwt.getClaim("phoneNumber").asString())
              .phoneNumberVerified(decodedJwt.getClaim("phoneNumberVerified").asBoolean())
              .picture(decodedJwt.getClaim("picture").asString())
              .gender(decodedJwt.getClaim("gender").asString())
              .address(decodedJwt.getClaim("address").asString())
              .tenantId(decodedJwt.getClaim("tenantId").asString())
              .platform(decodedJwt.getClaim("platform").asString())
              .signAt(decodedJwt.getClaim("signAt").asLong())
              .refreshAt(decodedJwt.getClaim("refreshAt").asLong())
              .roles(roles)
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
  public void clear(@Nonnull String token) throws SecretException {
    //    AuthUser authUser = analyze(token);
    //      Cache.set(
    //          getKey(token + ":logout"),
    //          String.valueOf(CurrentTimeMillis.now() / 1000),
    //          expire * 1000);
  }

  private String getKey(String token) {
    return authProperties.getCachePrefix() + Strings.COLON + token;
  }
}
