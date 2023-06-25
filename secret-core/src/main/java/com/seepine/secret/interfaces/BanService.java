package com.seepine.secret.interfaces;

import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.exception.BanSecretException;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * ban注解实现类
 *
 * @since 1.0.0
 * @author seepine
 */
public interface BanService {
  /**
   * 禁令取消
   *
   * @param authUser 用户信息
   * @param bans 功能
   * @throws BanSecretException 异常
   */
  void cancel(@Nonnull AuthUser authUser, @Nonnull String[] bans) throws BanSecretException;

  /**
   * 禁止
   *
   * @param authUser 用户信息
   * @param bans 功能
   * @param delayMillisecond 禁止时长
   * @throws BanSecretException 异常
   */
  void ban(@Nonnull AuthUser authUser, @Nonnull String[] bans, @Nonnegative long delayMillisecond)
      throws BanSecretException;

  /**
   * 校验，只要有一项未通过则校验失败
   *
   * @param authUser 用户信息
   * @param bans 功能
   * @throws BanSecretException 异常
   */
  void verify(@Nonnull AuthUser authUser, @Nonnull String[] bans) throws BanSecretException;
}
