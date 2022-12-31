package com.seepine.secret.interfaces;

import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.exception.AuthException;

/**
 * @author seepine
 * @since 0.0.6
 */
public interface AuthService {
  /**
   * 通过对象生成token，需要在此做token-user的缓存逻辑
   *
   * @param authUser 用户信息
   * @return token
   * @throws AuthException e
   */
  <T extends AuthUser> String genToken(T authUser) throws AuthException;
  /**
   * 获取缓存
   *
   * @param token token
   * @return 值
   * @throws AuthException e
   */
  <T extends AuthUser> T get(String token) throws AuthException;

  /**
   * 移除缓存逻辑，退出登录时会调用
   *
   * @param token token
   * @throws AuthException e
   */
  void remove(String token) throws AuthException;
}
