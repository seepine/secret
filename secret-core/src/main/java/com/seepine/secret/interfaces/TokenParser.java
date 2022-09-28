package com.seepine.secret.interfaces;

import com.seepine.secret.entity.AuthUser;

/**
 * 缓存接口
 *
 * @author seepine
 */
public interface TokenParser {
  /**
   * 通过token获取对象
   *
   * @param token token
   * @return 用户信息
   */
  <T extends AuthUser> T get(String token);

  /**
   * 保存用户信息，token可以通过authUser获取
   *
   * @param authUser 用户信息
   */
  <T extends AuthUser> void set(T authUser);
  /**
   * 通过对象生成token
   *
   * @param authUser 用户信息
   * @return 原值+token
   */
  <T extends AuthUser> T gen(T authUser);
  /**
   * 移除
   *
   * @param authUser 用户信息
   */
  <T extends AuthUser> void remove(T authUser);
}
