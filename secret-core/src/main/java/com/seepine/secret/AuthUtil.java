package com.seepine.secret;

import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.enums.AuthExceptionType;
import com.seepine.secret.exception.AuthException;
import com.seepine.secret.interfaces.AuthService;
import com.seepine.secret.properties.AuthProperties;
import com.seepine.tool.util.CurrentTimeMillis;
import com.seepine.tool.util.StrUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * @author seepine
 */
public class AuthUtil {
  private static final AuthUtil AUTH_UTIL = new AuthUtil();
  private AuthService authService;
  private final ThreadLocal<AuthUser> authUserThreadLocal = new ThreadLocal<>();
  private AuthProperties authProperties;

  public static void init(AuthProperties authProperties, AuthService authService) {
    AUTH_UTIL.authProperties = authProperties;
    AUTH_UTIL.authService = authService;
  }

  /**
   * 获取authProperties
   *
   * @return AuthProperties
   */
  public static AuthProperties getAuthProperties() {
    return AUTH_UTIL.authProperties;
  }

  public static void clear() {
    AUTH_UTIL.authUserThreadLocal.remove();
  }
  /**
   * 在controller/service中使用，直接获取当前登录者用户信息
   *
   * @param <T> 范型
   * @return user
   */
  @SuppressWarnings("unchecked")
  public static <T extends AuthUser> T getUser() {
    try {
      T user = (T) AUTH_UTIL.authUserThreadLocal.get();
      if (user != null) {
        return user;
      }
    } catch (Exception ignored) {
    }
    throw new AuthException(AuthExceptionType.INVALID_TOKEN);
  }
  /**
   * 获取用户权限
   *
   * @return permission
   */
  public static Set<String> getPermissions() {
    return getUser().getPermissions();
  }

  /**
   * 是否拥有某权限
   *
   * @param permission 权限值
   * @return 是/否
   */
  public static boolean hasPermission(String permission) {
    return getPermissions().contains(permission);
  }
  /**
   * 登录成功后设置用户信息，并返回token
   *
   * @param user user
   * @return token
   */
  public static <T extends AuthUser> T login(T user) {
    return login(user, user.getPermissions());
  }

  /**
   * 登录成功后设置用户信息，并返回token
   *
   * @param user user
   * @param permissions 用户权限
   * @return user with token
   */
  public static <T extends AuthUser> T login(T user, Set<String> permissions) {
    user.setPermissions(permissions == null ? new HashSet<>() : permissions);
    user.setSignTime(CurrentTimeMillis.now() / 1000);
    user.setRefreshTime(user.getSignTime());
    String token = AUTH_UTIL.authService.genToken(user);
    user.setToken(token);
    AUTH_UTIL.authUserThreadLocal.set(user);
    return user;
  }

  /**
   * 通过token获取用户信息
   *
   * @param token token
   * @return bool
   */
  public static boolean findAndFill(String token) {
    // 设置token
    if (StrUtil.isBlank(token)) {
      return false;
    }
    try {
      AuthUser authUser = AUTH_UTIL.authService.get(token);
      authUser.setToken(token);
      AUTH_UTIL.authUserThreadLocal.set(authUser);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 刷新token有效期
   *
   * @return authUser 用户信息
   */
  public static <T extends AuthUser> T refresh() {
    return refresh(getUser());
  }

  /**
   * 刷新token有效期且更新用户信息
   *
   * @param user 用户信息
   * @return 用户信息
   */
  public static <T extends AuthUser> T refresh(T user) {
    // fill refreshTime
    user.setRefreshTime(CurrentTimeMillis.now() / 1000);
    // fill permissions
    user.setPermissions(user.getPermissions() == null ? new HashSet<>() : user.getPermissions());
    user.setToken(null);
    user.setToken(AUTH_UTIL.authService.genToken(user));
    AUTH_UTIL.authUserThreadLocal.set(user);
    return user;
  }

  /** 登出 */
  public static void logout() {
    try {
      AUTH_UTIL.authService.remove(getUser().getToken());
      AUTH_UTIL.authUserThreadLocal.remove();
    } catch (AuthException ignored) {
    }
  }
}
