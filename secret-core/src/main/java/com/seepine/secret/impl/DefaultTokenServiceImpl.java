package com.seepine.secret.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.exception.TokenSecretException;
import com.seepine.secret.interfaces.TokenService;
import com.seepine.secret.properties.AuthProperties;
import com.seepine.tool.Run;
import com.seepine.tool.util.Objects;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author seepine
 * @since 0.0.6
 */
public class DefaultTokenServiceImpl implements TokenService {
	private final Algorithm algorithm;
	private final AuthProperties authProperties;
	private final JWTVerifier verifier;

	public DefaultTokenServiceImpl(AuthProperties authProperties) {
		this.authProperties = authProperties;
		this.algorithm = Algorithm.HMAC256(authProperties.getSecret());
		this.verifier = JWT.require(algorithm).withIssuer(authProperties.getIssuer()).build();
	}

	@Nonnull
	@Override
	public String generate(@Nonnull AuthUser authUser) throws TokenSecretException {
		try {
			Instant now = Instant.now();
			JWTCreator.Builder jwtBuilder =
				JWT.create().withIssuer(authProperties.getIssuer()).withNotBefore(now).withIssuedAt(now);
			if (authUser.getExpiresAt() != null) {
				jwtBuilder.withExpiresAt(Instant.ofEpochSecond(authUser.getExpiresAt()));
			}
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
			Run.nonEmpty(
				authUser.getRoles(),
				val -> jwtBuilder.withArrayClaim("roles", val.toArray(new String[]{})));
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
	public AuthUser analyze(@Nonnull String token) throws TokenSecretException {
		try {
			DecodedJWT decodedJwt = verifier.verify(token);
			// 获取roles
			Set<String> roles = new HashSet<>();
			if (!decodedJwt.getClaim("roles").isMissing() && !decodedJwt.getClaim("roles").isNull()) {
				roles = new HashSet<>(decodedJwt.getClaim("roles").asList(String.class));
			}
			return AuthUser.builder()
				.id(decodedJwt.getClaim("id").asString())
				.nickName(decodedJwt.getClaim("nickName").asString())
				.fullName(decodedJwt.getClaim("fullName").asString())
				.username(decodedJwt.getClaim("username").asString())
				.phone(decodedJwt.getClaim("phone").asString())
				.email(decodedJwt.getClaim("email").asString())
				.avatarUrl(decodedJwt.getClaim("avatarUrl").asString())
				.signAt(decodedJwt.getIssuedAt().getTime() / 1000)
				.expiresAt(
					Optional.ofNullable(decodedJwt.getExpiresAt())
						.map(item -> item.getTime() / 1000)
						.orElse(null))
				.refreshAt(decodedJwt.getClaim("refreshAt").asLong())
				.tenantId(decodedJwt.getClaim("tenantId").asString())
				.tenantName(decodedJwt.getClaim("tenantName").asString())
				.roles(roles)
				.claims(Objects.require(decodedJwt.getClaim("claims").asMap(), new HashMap<>(16)))
				.token(token)
				.build();
		} catch (Exception e) {
			throw new TokenSecretException(e);
		}
	}

	@Override
	public void clear(@Nonnull AuthUser authUser) throws TokenSecretException {
		// 此实现类使用jwt，因此无需清除
	}
}
