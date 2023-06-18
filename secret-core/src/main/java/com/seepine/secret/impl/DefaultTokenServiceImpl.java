package com.seepine.secret.impl;

import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.exception.TokenSecretException;
import com.seepine.secret.interfaces.TokenService;
import com.seepine.secret.properties.AuthProperties;
import com.seepine.tool.cache.Cache;
import com.seepine.tool.secure.symmetric.AES;
import com.seepine.tool.time.CurrentTimeMillis;
import com.seepine.tool.util.Strings;
import javax.annotation.Nonnull;

/**
 * @author seepine
 * @since 0.0.6
 */
public class DefaultTokenServiceImpl implements TokenService {
  private final AES aes;
  private final AuthProperties authProperties;

  public DefaultTokenServiceImpl(AuthProperties authProperties) {
    this.authProperties = authProperties;
    this.aes = new AES(authProperties.getSecret());
  }

  @Nonnull
  @Override
  public AuthUser generate(@Nonnull AuthUser authUser) throws TokenSecretException {
    String token = aes.encrypt(authUser.getId() + Strings.COLON + CurrentTimeMillis.now());
    authUser.setToken(token);
    long expireSecond = authUser.getExpiresAt() - authUser.getRefreshAt();
    Cache.set(authProperties.getCachePrefix() + token, authUser, expireSecond * 1000);
    return authUser;
  }

  @Nonnull
  @Override
  public AuthUser analyze(@Nonnull String token) throws TokenSecretException {
    AuthUser authUser;
    try {
      authUser = Cache.get(authProperties.getCachePrefix() + token);
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
  public AuthUser refresh(@Nonnull AuthUser authUser) {
    long expireSecond = authUser.getExpiresAt() - authUser.getRefreshAt();
    Cache.set(authProperties.getCachePrefix() + authUser.getToken(), authUser, expireSecond * 1000);
    return authUser;
  }

  @Override
  public void clear(@Nonnull AuthUser authUser) throws TokenSecretException {
    Cache.remove(authProperties.getCachePrefix() + authUser.getToken());
  }
}
