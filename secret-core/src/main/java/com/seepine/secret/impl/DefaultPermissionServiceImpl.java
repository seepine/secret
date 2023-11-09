package com.seepine.secret.impl;

import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.interfaces.PermissionService;
import com.seepine.secret.properties.AuthProperties;
import com.seepine.tool.cache.Cache;
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
  public Set<String> query(@Nonnull AuthUser authUser) {
    try {
      Set<String> find =
          Cache.get(
              authProperties.getCachePrefix() + Strings.COLON + authUser.getId() + ":permissions");
      if (find != null) {
        return find;
      }
    } catch (Exception ignore) {
    }
    return new HashSet<>();
  }
}
