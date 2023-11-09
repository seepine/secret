package com.seepine.secret.interfaces;

import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.exception.SecretException;
import javax.annotation.Nonnull;

/**
 * @author seepine
 * @since 0.0.6
 */
public interface TokenService {
  /**
   * 通过对象生成token，需要在此做token-user的缓存逻辑
   *
   * @param authUser 用户信息
   * @return 用户信息(需包含token)
   * @throws SecretException e
   */
  @Nonnull
  AuthUser generate(@Nonnull AuthUser authUser) throws SecretException;

  /**
   * 获取缓存
   *
   * @param token token
   * @return 值
   * @throws SecretException e
   */
  @Nonnull
  AuthUser analyze(@Nonnull String token) throws SecretException;

  /**
   * 刷新
   *
   * @param authUser 用户信息
   * @return 用户信息
   * @throws SecretException e
   */
  @Nonnull
  AuthUser refresh(@Nonnull AuthUser authUser) throws SecretException;
  /**
   * 清理逻辑，例如退出登录时会调用，清理缓存等
   *
   * @param authUser 用户信息
   * @throws SecretException e
   */
  void clear(@Nonnull AuthUser authUser) throws SecretException;
}
