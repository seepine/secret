package com.seepine.secret;

import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.enums.AuthExceptionType;
import com.seepine.secret.exception.AuthException;
import com.seepine.secret.interfaces.CacheService;
import com.seepine.secret.interfaces.TokenService;
import com.seepine.secret.properties.AuthProperties;
import com.seepine.tool.util.CurrentTimeMillis;
import com.seepine.tool.util.Objects;

import java.util.HashSet;
import java.util.Set;

/**
 * @author seepine
 */
public class AuthUtil {
  private static final AuthUtil AUTH_UTIL = new AuthUtil();
  private final ThreadLocal<AuthUser> authUserThreadLocal = new ThreadLocal<>();
  private AuthProperties authProperties;
  private TokenService tokenService;
  private CacheService cacheService;

  protected AuthUtil() {}

  public static void init(
      AuthProperties authProperties, TokenService tokenService, CacheService cacheService) {
    AUTH_UTIL.authProperties = authProperties;
    AUTH_UTIL.tokenService = tokenService;
    AUTH_UTIL.cacheService = cacheService;
  }

  /**
   * 获取authProperties
   *
   * @return AuthProperties
   */
  public static AuthProperties getAuthProperties() {
    return AUTH_UTIL.authProperties;
  }

  public static CacheService getCacheService() {
    return AUTH_UTIL.cacheService;
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
  public static AuthUser login(AuthUser user) {
    return login(user, user.getPermissions());
  }
  /**
   * 登录成功后设置用户信息，并返回token
   *
   * @param user user
   * @param permissions 用户权限
   * @return user with token
   */
  public static AuthUser login(AuthUser user, Set<String> permissions) {
    user.setPermissions(permissions == null ? new HashSet<>() : permissions);
    user.setSignTime(CurrentTimeMillis.now() / 1000);
    user.setRefreshTime(user.getSignTime());
    String token = AUTH_UTIL.tokenService.generate(user);
    user.setToken(token);
    AuthUser copy = user.copy();
    AUTH_UTIL.authUserThreadLocal.set(copy);
    return copy;
  }

  /**
   * 通过token获取用户信息
   *
   * @param token token
   * @return bool
   */
  public static boolean findAndFill(String token) {
    // 设置token
    if (Objects.isBlank(token)) {
      return false;
    }
    try {
      AUTH_UTIL.authUserThreadLocal.set(AUTH_UTIL.tokenService.analyze(token));
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
  public static AuthUser refresh() {
    return refresh(getUser());
  }

  /**
   * 刷新token有效期且更新用户信息
   *
   * @param user 用户信息
   * @return 用户信息
   */
  public static AuthUser refresh(AuthUser user) {
    // fill refreshTime
    user.setRefreshTime(CurrentTimeMillis.now() / 1000);
    // fill permissions
    user.setPermissions(user.getPermissions() == null ? new HashSet<>() : user.getPermissions());
    user.setToken(null);
    user.setToken(AUTH_UTIL.tokenService.generate(user));
    AuthUser copy = user.copy();
    AUTH_UTIL.authUserThreadLocal.set(copy);
    return copy;
  }

  /** 登出 */
  public static void logout() {
    try {
      AUTH_UTIL.tokenService.clear(getUser().getToken());
      AUTH_UTIL.authUserThreadLocal.remove();
    } catch (AuthException ignored) {
    }
  }
}
