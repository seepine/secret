package com.seepine.secret;

import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.enums.AuthExceptionType;
import com.seepine.secret.exception.AuthException;
import com.seepine.secret.interfaces.TokenParser;
import com.seepine.secret.properties.AuthProperties;
import com.seepine.tool.util.StrUtil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author seepine
 */
public class AuthUtil {
  private static final AuthUtil AUTH_UTIL = new AuthUtil();
  private TokenParser tokenParser;
  private final ThreadLocal<AuthUser> authUserThreadLocal = new ThreadLocal<>();
  private AuthProperties authProperties;

  public static void init(AuthProperties authProperties, TokenParser tokenParser) {
    AUTH_UTIL.authProperties = authProperties;
    AUTH_UTIL.tokenParser = tokenParser;
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
   * @param permission 用户权限
   * @return user with token
   */
  public static <T extends AuthUser> T login(T user, Set<String> permission) {
    user.setPermissions(permission == null ? new HashSet<>() : permission);
    user.setSignTime(LocalDateTime.now());
    T authUser = AUTH_UTIL.tokenParser.gen(user);
    AUTH_UTIL.tokenParser.set(authUser);
    return authUser;
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
      AuthUser authUser = AUTH_UTIL.tokenParser.get(token);
      AUTH_UTIL.authUserThreadLocal.set(authUser);
      if (AUTH_UTIL.authProperties.getResetTimeout() > 0) {
        Duration duration = Duration.between(authUser.getRefreshTime(), LocalDateTime.now());
        if (duration.toSeconds() > AUTH_UTIL.authProperties.getResetTimeout()) {
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
    T authUser = getUser();
    user.setToken(authUser.getToken());
    user.setRefreshTime(LocalDateTime.now());
    AUTH_UTIL.tokenParser.set(user);
    return user;
  }

  /** 登出 */
  public static void logout() {
    AuthUser user = AUTH_UTIL.authUserThreadLocal.get();
    if (user != null) {
      AUTH_UTIL.tokenParser.remove(user);
    }
  }
}
