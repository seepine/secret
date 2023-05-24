package com.seepine.secret.interfaces;

import com.seepine.secret.entity.AuthUser;
import java.util.Set;
import javax.annotation.Nonnull;

public interface PermissionService {
  /**
   * 获取权限列表
   *
   * @param user 用户信息
   * @return 权限列表
   */
  @Nonnull
  Set<String> get(@Nonnull AuthUser user);

  /**
   * 设置权限列表
   *
   * @param user 用户信息
   * @param permissions 权限列表
   */
  void set(@Nonnull AuthUser user, @Nonnull Set<String> permissions);
}
