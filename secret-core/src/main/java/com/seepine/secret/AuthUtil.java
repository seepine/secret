package com.seepine.secret;

import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.enums.AuthExceptionType;
import com.seepine.secret.exception.AuthException;
import com.seepine.secret.interfaces.Cache;
import com.seepine.secret.interfaces.TokenGenerator;
import com.seepine.secret.properties.AuthProperties;
import com.seepine.tool.util.StrUtil;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
/**
 * @author seepine
 */
public class AuthUtil {
  private static final AuthUtil AUTH_UTIL = new AuthUtil();
  private static final String COLON = ":";
  private AuthProperties authProperties;

  private Cache cache;
  private TokenGenerator tokenGenerator;

  private final ThreadLocal<String> THREAD_LOCAL_TOKEN = new ThreadLocal<>();
  private final ThreadLocal<AuthUser> THREAD_LOCAL_USER = new ThreadLocal<>();

  public static void init(
      AuthProperties authProperties, TokenGenerator tokenGenerator, Cache cache) {
    AUTH_UTIL.authProperties = authProperties;
    AUTH_UTIL.tokenGenerator = tokenGenerator;
    AUTH_UTIL.cache = cache;
  }

  public static void setProperties(AuthProperties authProperties) {
    AUTH_UTIL.authProperties = authProperties;
  }

  public static void setTokenGenerator(TokenGenerator tokenGenerator) {
    AUTH_UTIL.tokenGenerator = tokenGenerator;
  }

  public static void setAuthCache(Cache cache) {
    AUTH_UTIL.cache = cache;
  }

  public static void clear() {
    AUTH_UTIL.THREAD_LOCAL_USER.remove();
    AUTH_UTIL.THREAD_LOCAL_TOKEN.remove();
  }

  /**
   * 获取用户缓存key
   *
   * @return com.seepine.auth:{token}:user
   */
  private String getUserKey(String token) {
    return authProperties.getCachePrefix() + COLON + token + COLON + "user";
  }

  /**
   * 获取权限缓存key
   *
   * @return com.seepine.auth:{token}:permission
   */
  private String getPermissionKey(String token) {
    return authProperties.getCachePrefix() + COLON + token + COLON + "permission";
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
      T user = (T) AUTH_UTIL.THREAD_LOCAL_USER.get();
      if (user != null) {
        return user;
      }
    } catch (Exception ignored) {
    }
    if (StrUtil.isNotBlank(AUTH_UTIL.THREAD_LOCAL_TOKEN.get())) {
      AUTH_UTIL.cache.remove(AUTH_UTIL.getUserKey(AUTH_UTIL.THREAD_LOCAL_TOKEN.get()));
    }
    throw new AuthException(AuthExceptionType.EXPIRED_LOGIN);
  }
  /**
   * 获取用户权限
   *
   * @return permission
   */
  public static List<String> getPermissions() {
    try {
      AuthUser authUser = AUTH_UTIL.THREAD_LOCAL_USER.get();
      if (authUser == null) {
        return new ArrayList<>();
      }
      return authUser.getPermissions() == null ? new ArrayList<>() : authUser.getPermissions();
    } catch (Exception ignored) {
    }
    if (StrUtil.isNotBlank(AUTH_UTIL.THREAD_LOCAL_TOKEN.get())) {
      AUTH_UTIL.cache.remove(AUTH_UTIL.getPermissionKey(AUTH_UTIL.THREAD_LOCAL_TOKEN.get()));
    }
    throw new AuthException(AuthExceptionType.EXPIRED_LOGIN);
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
   * @param permission 用户权限
   * @return token
   */
  public static <T extends AuthUser> T login(T user, List<String> permission) {
    String token = AUTH_UTIL.tokenGenerator.gen();
    user.setPermissions(permission);
    user.setAccessToken(token);
    AUTH_UTIL.putIntoCache(token, user);
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
    AUTH_UTIL.THREAD_LOCAL_TOKEN.set(token);
    // 用户相关
    AuthUser user;
    try {
      user = (AuthUser) AUTH_UTIL.cache.get(AUTH_UTIL.getUserKey(token));
    } catch (Exception e) {
      return false;
    }
    if (user == null) {
      return false;
    }
    AUTH_UTIL.THREAD_LOCAL_USER.set(user);
    // 刷新缓存
    if (AUTH_UTIL.authProperties.getResetTimeout()) {
      refresh();
    }
    return true;
  }

  /**
   * 刷新用户信息,当进行了更新等操作 支持该方法为未登录线程赋值user对象，提供getUser()取值用
   *
   * @param user user
   */
  public static <T extends AuthUser> void refresh(T user) {
    AUTH_UTIL.putIntoCache(AUTH_UTIL.THREAD_LOCAL_TOKEN.get(), user);
  }

  /** 主动刷新缓存 */
  public static void refresh() {
    String token = AUTH_UTIL.THREAD_LOCAL_TOKEN.get();
    if (StrUtil.isNotBlank(token)) {
      // 刷新用户信息缓存
      AUTH_UTIL.cache.expire(
          AUTH_UTIL.getUserKey(token), Duration.ofSeconds(AUTH_UTIL.authProperties.getTimeout()));
      // 刷新用户权限缓存
      AUTH_UTIL.cache.expire(
          AUTH_UTIL.getPermissionKey(token),
          Duration.ofSeconds(AUTH_UTIL.authProperties.getTimeout()));
    }
  }

  /**
   * 供登录或刷新用户及权限方法用
   *
   * @param token token
   * @param user 用户信息
   */
  private <T extends AuthUser> void putIntoCache(String token, T user) {
    if (StrUtil.isNotBlank(token)) {
      THREAD_LOCAL_TOKEN.set(token);
      if (user != null) {
        cache.set(getUserKey(token), user);
      }
      refresh();
    }
    if (user != null) {
      THREAD_LOCAL_USER.set(user);
    }
  }

  /** 登出 */
  public static void logout() {
    String token = AUTH_UTIL.THREAD_LOCAL_TOKEN.get();
    if (StrUtil.isNotBlank(token)) {
      AUTH_UTIL.cache.remove(AUTH_UTIL.getUserKey(token));
    }
  }
}
