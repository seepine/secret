package com.seepine.secret.impl;

import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.interfaces.PermissionService;
import com.seepine.secret.properties.AuthProperties;
import com.seepine.tool.cache.Cache;
import com.seepine.tool.util.Objects;
import com.seepine.tool.util.Strings;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nonnull;

public class DefaultPermissionServiceImpl implements PermissionService {
  private final AuthProperties authProperties;

  public DefaultPermissionServiceImpl(AuthProperties authProperties) {
    this.authProperties = authProperties;
  }

  @Nonnull
  @Override
  public Set<String> get(@Nonnull AuthUser user) {
    try {
      return Objects.require(Cache.get(getCacheKey() + user.getId()), HashSet::new);
    } catch (Exception e) {
      return new HashSet<>();
    }
  }

  @Override
  public void set(@Nonnull AuthUser user, @Nonnull Set<String> permissions) {
    long delaySecond = 0;
    if (user.getExpiresAt() != null) {
      delaySecond = user.getExpiresAt() - user.getRefreshAt();
    }
    Cache.set(getCacheKey() + user.getId(), permissions, delaySecond * 1000);
  }

  private String getCacheKey() {
    String prefix =
        authProperties.getCachePrefix().endsWith(Strings.COLON)
            ? authProperties.getCachePrefix()
            : authProperties.getCachePrefix() + Strings.COLON;
    return prefix + "permissions:";
  }
}
