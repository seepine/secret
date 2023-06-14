package com.seepine.secret;

import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.exception.SecretException;
import com.seepine.secret.exception.UnauthorizedSecretException;
import com.seepine.secret.interfaces.TokenService;
import com.seepine.secret.properties.AuthProperties;
import com.seepine.secret.util.PermissionUtil;
import com.seepine.tool.Run;
import com.seepine.tool.time.CurrentTimeMillis;
import com.seepine.tool.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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

  protected AuthUtil() {
  }

  public static void init(AuthProperties authProperties, TokenService tokenService) {
    AUTH_UTIL.authProperties = authProperties;
    AUTH_UTIL.tokenService = tokenService;
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
    return login(user, user.getPermissions(), null);
  }

  /**
   * 登录成功后设置用户信息，并返回token
   *
   * @param user          user
   * @param expiresSecond 过期时间(秒)
   * @return token
   */
  @Nonnull
  public static AuthUser login(@Nonnull AuthUser user, Long expiresSecond) {
    return login(user, user.getPermissions(), expiresSecond);
  }

  /**
   * 登录成功后设置用户信息，并返回token
   *
   * @param user        user
   * @param permissions 用户权限
   * @return user with token
   */
  @Nonnull
  public static AuthUser login(@Nonnull AuthUser user, @Nullable Set<String> permissions) {
    return login(user, permissions, null);
  }

  /**
   * 登录成功后设置用户信息，并返回token
   *
   * @param user          user
   * @param permissions   用户权限
   * @param expiresSecond 过期时间
   * @return user with token
   */
  @Nonnull
  public static AuthUser login(@Nonnull AuthUser user, @Nullable Set<String> permissions, @Nullable Long expiresSecond) {
    user.setPermissions(Objects.require(permissions, HashSet::new));
    user.setSignAt(CurrentTimeMillis.now() / 1000);
    user.setRefreshAt(user.getSignAt());
    Long expires = Objects.require(expiresSecond, getAuthProperties().getExpiresSecond());
    Run.nonNull(expires, val -> {
      if (val > 0) {
        user.setExpiresAt(user.getRefreshAt() + val);
      }
    });
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
  public static boolean findAndFill(@Nullable String token) {
    // 设置token
    if (Objects.isBlank(token)) {
      return false;
    }
    try {
      AuthUser authUser = AUTH_UTIL.tokenService.analyze(token);
      authUser.setToken(token);
      AUTH_UTIL.authUserThreadLocal.set(authUser);
      return true;
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
    AuthUser copy = user.copy();
    // fill refreshTime
    copy.setRefreshAt(CurrentTimeMillis.now() / 1000);
    Run.nonNull(getAuthProperties().getExpiresSecond(), val -> {
      if (val > 0) {
        copy.setExpiresAt(copy.getRefreshAt() + getAuthProperties().getExpiresSecond());
      }
    });
    AUTH_UTIL.authUserThreadLocal.set(copy);
    return copy;
  }

  /**
   * 登出
   */
  public static void logout() {
    try {
      AUTH_UTIL.tokenService.clear(getUser());
      AUTH_UTIL.authUserThreadLocal.remove();
    } catch (SecretException ignored) {
    }
  }
}
