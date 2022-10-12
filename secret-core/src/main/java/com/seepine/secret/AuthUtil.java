package com.seepine.secret;

import com.fasterxml.jackson.core.type.TypeReference;
import com.seepine.json.Json;
import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.enums.AuthExceptionType;
import com.seepine.secret.exception.AuthException;
import com.seepine.secret.interfaces.AuthCache;
import com.seepine.secret.interfaces.AuthTokenGen;
import com.seepine.secret.properties.AuthProperties;
import com.seepine.secret.util.CurrentTimeMillis;
import com.seepine.tool.util.StrUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * @author seepine
 */
public class AuthUtil {
  private static final AuthUtil AUTH_UTIL = new AuthUtil();
  private AuthTokenGen tokenGen;
  private AuthCache cache;
  private final ThreadLocal<AuthUser> authUserThreadLocal = new ThreadLocal<>();
  private AuthProperties authProperties;

  public static void init(AuthProperties authProperties, AuthCache cache, AuthTokenGen tokenGen) {
    AUTH_UTIL.authProperties = authProperties;
    AUTH_UTIL.cache = cache;
    AUTH_UTIL.tokenGen = tokenGen;
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
   * 创建新对象避免外部操作对象修改数据
   *
   * @param user 用户
   * @param permissions 权限
   * @return 用户信息
   * @param <T> T
   */
  private static <T extends AuthUser> T createNew(T user, Set<String> permissions) {
    Set<String> permissionsCache = permissions == null ? new HashSet<>() : permissions;
    user.setPermissions(null);
    T newUser = Json.parse(Json.toJson(user), new TypeReference<>() {});
    newUser.setPermissions(permissionsCache);
    newUser.setToken(null);
    return newUser;
  }

  /**
   * 登录成功后设置用户信息，并返回token
   *
   * @param user user
   * @param permissions 用户权限
   * @return user with token
   */
  public static <T extends AuthUser> T login(T user, Set<String> permissions) {
    T newUser = createNew(user, permissions);
    newUser.setSignTime(CurrentTimeMillis.now());
    newUser.setRefreshTime(newUser.getSignTime());
    String token = AUTH_UTIL.tokenGen.gen(newUser);
    AUTH_UTIL.cache.set(AUTH_UTIL.authProperties.getCachePrefix() + token, newUser);
    newUser.setToken(token);
    return newUser;
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
      AuthUser authUser = AUTH_UTIL.cache.get(AUTH_UTIL.authProperties.getCachePrefix() + token);
      authUser.setToken(token);
      AUTH_UTIL.authUserThreadLocal.set(authUser);
      if (AUTH_UTIL.authProperties.getResetTimeout() > 0) {
        if (CurrentTimeMillis.now() - authUser.getRefreshTime()
            > AUTH_UTIL.authProperties.getResetTimeout() * 1000) {
          refresh();
        }
      }
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
    String token = getUser().getToken();
    T newUser = createNew(user, user.getPermissions());
    newUser.setRefreshTime(CurrentTimeMillis.now());
    AUTH_UTIL.cache.set(AUTH_UTIL.authProperties.getCachePrefix() + token, newUser);
    newUser.setToken(token);
    AUTH_UTIL.authUserThreadLocal.set(newUser);
    return newUser;
  }

  /** 登出 */
  public static void logout() {
    try {
      AUTH_UTIL.cache.remove(AUTH_UTIL.authProperties.getCachePrefix() + getUser().getToken());
      AUTH_UTIL.authUserThreadLocal.remove();
    } catch (AuthException ignored) {
    }
  }
}
