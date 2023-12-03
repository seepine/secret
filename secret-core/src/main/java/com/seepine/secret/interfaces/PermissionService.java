package com.seepine.secret.interfaces;

import com.seepine.secret.entity.AuthUser;
import java.util.Set;
import javax.annotation.Nonnull;

public interface PermissionService {
  /**
   * 查询用户权限
   *
   * <p>在实现方法中使用http请求接口，接口上需添加@Expose(skip=true)，否则将会无限循环
   *
   * @param authUser 用户信息
   * @return 权限集合
   */
  @Nonnull
  Set<String> query(@Nonnull AuthUser authUser);
}
