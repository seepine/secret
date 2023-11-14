package com.seepine.secret.impl;

import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.exception.SecretException;
import com.seepine.secret.exception.TokenSecretException;
import com.seepine.secret.interfaces.TokenService;
import com.seepine.secret.properties.AuthProperties;
import com.seepine.tool.cache.Cache;
import com.seepine.tool.secure.digest.Digests;
import com.seepine.tool.secure.digest.HmacAlgorithm;
import com.seepine.tool.time.CurrentTimeMillis;
import com.seepine.tool.util.Strings;
import javax.annotation.Nonnull;

/**
 * 传统形式的token
 *
 * @author seepine
 */
public class NormalTokenServiceImpl implements TokenService {

  private final AuthProperties authProperties;

  public NormalTokenServiceImpl(AuthProperties authProperties) {
    this.authProperties = authProperties;
  }

  @Nonnull
  @Override
  public AuthUser generate(@Nonnull AuthUser authUser) throws SecretException {
    String token =
        Digests.hmac(HmacAlgorithm.HmacSHA256, authProperties.getSecret())
            .digestHex(authUser.getId() + Strings.COLON + CurrentTimeMillis.now());
    authUser.setToken(token);
    long expireSecond = authUser.getExpiresAt() - authUser.getRefreshAt();
    Cache.set(getKey(token), authUser, expireSecond * 1000);
    return authUser;
  }

  @Nonnull
  @Override
  public AuthUser analyze(@Nonnull String token) throws SecretException {
    AuthUser authUser;
    try {
      authUser = Cache.get(getKey(token));
    } catch (Exception e) {
      throw new TokenSecretException(e);
    }
    if (authUser == null) {
      throw new TokenSecretException();
    }
    return authUser;
  }

  @Nonnull
  @Override
  public AuthUser refresh(@Nonnull AuthUser authUser) throws SecretException {
    long expireSecond = authUser.getExpiresAt() - authUser.getRefreshAt();
    Cache.set(getKey(authUser.getToken()), authUser, expireSecond * 1000);
    return authUser;
  }

  @Override
  public void clear(@Nonnull AuthUser authUser) throws SecretException {
    Cache.remove(getKey(authUser.getToken()));
  }

  private String getKey(String token) {
    return authProperties.getCachePrefix() + Strings.COLON + token;
  }
}