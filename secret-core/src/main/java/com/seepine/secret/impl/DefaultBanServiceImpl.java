package com.seepine.secret.impl;

import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.exception.BanSecretException;
import com.seepine.secret.interfaces.BanService;
import com.seepine.secret.properties.AuthProperties;
import com.seepine.tool.cache.Cache;
import com.seepine.tool.util.Strings;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * 通过用户id禁用
 *
 * @since 1.0.0
 */
public class DefaultBanServiceImpl implements BanService {

  private final AuthProperties authProperties;

  public DefaultBanServiceImpl(AuthProperties authProperties) {
    this.authProperties = authProperties;
  }

  @Override
  public void cancel(@Nonnull AuthUser authUser, @Nonnull String[] bans) throws BanSecretException {
    for (String ban : bans) {
      String key = getKey(authUser, ban);
      Cache.remove(key);
    }
  }

  @Override
  public void ban(
      @Nonnull AuthUser authUser, @Nonnull String[] bans, @Nonnegative long delayMillisecond)
      throws BanSecretException {
    for (String ban : bans) {
      String key = getKey(authUser, ban);
      if (delayMillisecond > 0) {
        Cache.set(key, ban, delayMillisecond);
      } else {
        Cache.set(key, ban);
      }
    }
  }

  @Override
  public void verify(@Nonnull AuthUser authUser, @Nonnull String[] bans) throws BanSecretException {
    for (String ban : bans) {
      String key = getKey(authUser, ban);
      // 只要有一项不通过，则不通过
      if (Cache.get(key) != null) {
        throw new BanSecretException(ban, ban + " be banned");
      }
    }
  }

  private String getKey(@Nonnull AuthUser authUser, @Nonnull String ban) throws BanSecretException {
    if (authUser.getId() == null) {
      throw new BanSecretException("user id cannot be null");
    }
    return authProperties.getCachePrefix()
        + Strings.COLON
        + "ban"
        + Strings.COLON
        + authUser.getId()
        + Strings.COLON
        + ban;
  }
}
