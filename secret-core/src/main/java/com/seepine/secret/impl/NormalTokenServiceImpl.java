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
import javax.annotation.Nonnegative;
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
  public String generate(@Nonnull final AuthUser authUser, @Nonnegative final Long expires)
      throws SecretException {
    String token =
        Digests.hmac(HmacAlgorithm.HmacSHA256, authProperties.getSecret())
            .digestHex(authUser.getId() + Strings.COLON + CurrentTimeMillis.now());
    Cache.set(getKey(token), authUser, expires * 1000);
    return token;
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

  @Override
  public void clear(@Nonnull String token) throws SecretException {
    Cache.remove(getKey(token));
  }

  private String getKey(String token) {
    return authProperties.getCachePrefix() + Strings.COLON + token;
  }
}
