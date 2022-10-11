package com.seepine.secret.interfaces;

import com.seepine.secret.entity.AuthUser;

/**
 * 缓存接口
 *
 * @author seepine
 */
public interface AuthTokenGen {
  /**
   * 通过对象生成token
   *
   * @param authUser 用户信息
   * @return token
   */
  <T extends AuthUser> String gen(T authUser);
}
