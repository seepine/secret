package com.seepine.secret.interfaces;

import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.exception.AuthException;

/**
 * @author seepine
 * @since 0.0.6
 */
public interface TokenService {
  /**
   * 通过对象生成token，需要在此做token-user的缓存逻辑
   *
   * @param authUser 用户信息
   * @return token
   * @throws AuthException e
   */
  String generate(AuthUser authUser) throws AuthException;
  /**
   * 获取缓存
   *
   * @param token token
   * @return 值
   * @throws AuthException e
   */
  AuthUser analyze(String token) throws AuthException;
  /**
   * 清理逻辑，例如退出登录时会调用，清理缓存等，例如jwt还需要额外增加登录黑名单
   *
   * @param token token
   * @throws AuthException e
   */
  void clear(String token) throws AuthException;
}
