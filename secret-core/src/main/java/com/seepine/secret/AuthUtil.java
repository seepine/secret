package com.seepine.secret;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.entity.TokenInfo;
import com.seepine.secret.exception.BanSecretException;
import com.seepine.secret.exception.SecretException;
import com.seepine.secret.exception.UnauthorizedSecretException;
import com.seepine.secret.interfaces.BanService;
import com.seepine.secret.interfaces.PermissionService;
import com.seepine.secret.interfaces.TokenService;
import com.seepine.secret.properties.AuthProperties;
import com.seepine.secret.util.PermissionUtil;
import com.seepine.tool.time.CurrentTime;
import com.seepine.tool.util.Objects;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author seepine
 */
public class AuthUtil {
  private static final AuthUtil AUTH_UTIL = new AuthUtil();
  private final ThreadLocal<AuthUser> authUserThreadLocal = new TransmittableThreadLocal<>();
  private AuthProperties authProperties;
  private TokenService tokenService;
  private PermissionService permissionService;
  private BanService banService;

  protected AuthUtil() {}

  public static void init(
      AuthProperties authProperties,
      TokenService tokenService,
      PermissionService permissionService,
      BanService banService) {
    AUTH_UTIL.authProperties = authProperties;
    AUTH_UTIL.tokenService = tokenService;
    AUTH_UTIL.permissionService = permissionService;
    AUTH_UTIL.banService = banService;
  }

  /**
   * 获取authProperties
   *
   * @return AuthProperties
   */
  public static AuthProperties getAuthProperties() {
    return AUTH_UTIL.authProperties;
  }

  public static PermissionService getPermissionService() {
    return AUTH_UTIL.permissionService;
  }

  public static void clear() {
    AUTH_UTIL.authUserThreadLocal.remove();
  }

  /**
   * 在controller/service中使用，直接获取当前登录者用户信息
   *
   * @return user
   */
  public static AuthUser getUser() {
    try {
      AuthUser user = AUTH_UTIL.authUserThreadLocal.get();
      if (user != null) {
        return user;
      }
    } catch (Exception ignored) {
    }
    throw new UnauthorizedSecretException();
  }

  /**
   * 获取用户权限
   *
   * @return permissions
   */
  @Nonnull
  public static Set<String> getPermissions() {
    return getUser().getPermissions();
  }

  /**
   * 是否拥有某权限
   *
   * @param permission 权限值
   * @return 是/否
   */
  public static boolean hasPermission(@Nonnull String... permission) {
    try {
      PermissionUtil.verify(getPermissions(), permission, null);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 获取用户角色
   *
   * @return roles
   */
  @Nonnull
  public static Set<String> getRoles() {
    return Objects.require(getUser().getRoles(), HashSet::new);
  }

  /**
   * 是否拥有某权限
   *
   * @param role 权限值
   * @return 是/否
   */
  public static boolean hasRole(@Nonnull String... role) {
    try {
      PermissionUtil.verify(getRoles(), role, null);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 登录成功后设置用户信息，并返回token
   *
   * @param user user
   * @return token
   */
  @Nonnull
  public static AuthUser login(@Nonnull AuthUser user) {
    return login(user, getAuthProperties().getExpires(), getAuthProperties().getRefreshExpires());
  }

  /**
   * 登录成功后设置用户信息，并返回token
   *
   * @param user user
   * @param expiresSecond 过期时间,不允许小于0
   * @return user with token
   */
  @Nonnull
  public static AuthUser login(@Nonnull AuthUser user, @Nonnegative Integer expiresSecond) {
    return login(user, Long.valueOf(expiresSecond), getAuthProperties().getRefreshExpires());
  }

  /**
   * 登录成功后设置用户信息，并返回token
   *
   * @param user user
   * @param expiresSecond 过期时间,不允许小于0
   * @param refreshExpiresSecond 刷新token过期时间,不允许小于0
   * @return user with token
   */
  @Nonnull
  public static AuthUser login(
      @Nonnull AuthUser user,
      @Nonnegative Integer expiresSecond,
      @Nonnegative Integer refreshExpiresSecond) {
    return login(user, Long.valueOf(expiresSecond), Long.valueOf(refreshExpiresSecond));
  }

  /**
   * 登录成功后设置用户信息，并返回token
   *
   * @param user user
   * @param expiresSecond 过期时间,不允许小于0
   * @param refreshExpiresSecond 刷新token过期时间,不允许小于0
   * @return user with token
   */
  @Nonnull
  public static AuthUser login(
      @Nonnull AuthUser user,
      @Nonnegative Long expiresSecond,
      @Nonnegative Long refreshExpiresSecond) {
    AuthUser copy = user.copy();
    copy.setPermissions(Objects.require(copy.getPermissions(), HashSet::new));
    if (copy.getTokenInfo() == null) {
      copy.setTokenInfo(new TokenInfo().setSignAt(CurrentTime.second()));
    }
    copy.setTokenInfo(
        copy.getTokenInfo()
            .setExpires(expiresSecond)
            .setRefreshExpires(refreshExpiresSecond)
            .setRefreshAt(CurrentTime.second()));
    copy.getTokenInfo().setAccessToken(null);
    copy.getTokenInfo().setRefreshToken(null);
    String accessToken = AUTH_UTIL.tokenService.generate(copy.copy(), expiresSecond);
    String refreshToken = AUTH_UTIL.tokenService.generate(copy.copy(), refreshExpiresSecond);
    copy.setTokenInfo(
        copy.getTokenInfo().setAccessToken(accessToken).setRefreshToken(refreshToken));
    AUTH_UTIL.authUserThreadLocal.set(copy);
    return copy;
  }

  /**
   * 通过token获取用户信息
   *
   * @param token token
   * @return bool
   */
  public static boolean findAndFill(@Nullable String token) {
    // 设置token
    if (Objects.isBlank(token)) {
      return false;
    }
    try {
      AuthUser authUser = AUTH_UTIL.tokenService.analyze(token);
      AUTH_UTIL.authUserThreadLocal.set(authUser);
      return true;
    } catch (SecretException e) {
      throw e;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 刷新token
   *
   * @return authUser 用户信息
   */
  @Nonnull
  public static AuthUser refresh() {
    return refresh(getUser());
  }

  /**
   * 刷新token且更新用户信息
   *
   * @param user 用户信息
   * @return 用户信息
   */
  @Nonnull
  public static AuthUser refresh(@Nonnull AuthUser user) {
    return login(user, user.getTokenInfo().getExpires(), user.getTokenInfo().getRefreshExpires());
  }

  /**
   * 使用特定用户执行
   *
   * @code AuthUtil.execute(user, ()->{ // AuthUtil.getUser() 会是传入的 user });
   * @param authUser 用户信息
   * @param runnable 执行方法
   */
  public static void execute(@Nonnull AuthUser authUser, @Nonnull Runnable runnable) {
    AuthUser back = AUTH_UTIL.authUserThreadLocal.get();
    AUTH_UTIL.authUserThreadLocal.set(authUser);
    runnable.run();
    AUTH_UTIL.authUserThreadLocal.set(back);
  }

  /**
   * 使用特定用户执行
   *
   * @code AuthUtil.execute(user, ()->{ // AuthUtil.getUser() 会是传入的 user });
   * @param authUser 用户信息
   * @param supplier 执行方法
   */
  public static <T> T execute(@Nonnull AuthUser authUser, @Nonnull Supplier<T> supplier) {
    AuthUser back = AUTH_UTIL.authUserThreadLocal.get();
    try {
      AUTH_UTIL.authUserThreadLocal.set(authUser);
      return supplier.get();
    } finally {
      AUTH_UTIL.authUserThreadLocal.set(back);
    }
  }

  /** 登出 */
  public static void logout() {
    try {
      AUTH_UTIL.tokenService.clear(getUser());
      AUTH_UTIL.authUserThreadLocal.remove();
    } catch (SecretException ignored) {
    }
  }

  /**
   * 永久禁用
   *
   * @param bans 功能
   */
  public static void ban(@Nonnull String... bans) throws BanSecretException {
    AUTH_UTIL.banService.ban(getUser(), bans, 0);
  }

  /**
   * 禁用
   *
   * @param bans 功能
   * @param delayMillisecond 禁用时长，0则永久
   */
  public static void ban(@Nonnull String[] bans, @Nonnegative long delayMillisecond)
      throws BanSecretException {
    AUTH_UTIL.banService.ban(getUser(), bans, delayMillisecond);
  }

  /**
   * 允许功能
   *
   * @param bans 功能
   */
  public static void banCancel(@Nonnull String... bans) throws BanSecretException {
    AUTH_UTIL.banService.cancel(getUser(), bans);
  }

  /**
   * 验证
   *
   * @param bans 功能
   * @return true则验证通过，false则表示有某一项被禁
   */
  public static boolean banVerify(@Nonnull String... bans) {
    try {
      AUTH_UTIL.banService.verify(getUser(), bans);
      return true;
    } catch (BanSecretException ignore) {
      return false;
    }
  }

  /**
   * 验证
   *
   * @param bans 功能
   * @throws BanSecretException e
   */
  public static void banVerifyOrElseThrow(@Nonnull String... bans) throws BanSecretException {
    AUTH_UTIL.banService.verify(getUser(), bans);
  }
}
