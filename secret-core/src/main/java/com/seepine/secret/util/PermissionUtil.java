package com.seepine.secret.util;

import com.seepine.secret.AuthUtil;
import com.seepine.secret.annotation.Permission;
import com.seepine.secret.annotation.PermissionPrefix;
import com.seepine.secret.enums.AuthExceptionType;
import com.seepine.secret.exception.AuthException;
import com.seepine.tool.util.Objects;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author seepine
 * @since 2.0.0
 */
public class PermissionUtil {
  public static Permission getPermission(Method method) {
    Permission permission = null;
    if (method.isAnnotationPresent(Permission.class)) {
      permission = method.getAnnotation(Permission.class);
    } else {
      try {
        // 1.判断是否重写父类方法
        Method parentMethod =
            method.getDeclaringClass().getSuperclass().getDeclaredMethod(method.getName());
        // 2.是的话判断父类的方法是否有注解
        if (parentMethod.isAnnotationPresent(Permission.class)) {
          permission = parentMethod.getAnnotation(Permission.class);
        }
      } catch (NoSuchMethodException ignored) {
      }
    }
    return permission;
  }

  public static void verify(Permission permission, PermissionPrefix permissionPrefix) {
    Set<String> permissions = AuthUtil.getPermissions();

    // 如果类上有前缀注解，并且值不为空串，并且权限注解prefix为true，表示需要拼接前缀
    boolean hasPrefix =
        permissionPrefix != null
            && Objects.nonBlank(permissionPrefix.value())
            && permission.prefix();

    // 1.校验必须包含的权限
    List<String> must = Arrays.asList(permission.value());
    if (hasPrefix) {
      must =
          must.stream().map(item -> permissionPrefix.value() + item).collect(Collectors.toList());
    }
    // 如果权限不为空，并且没有拥有所有需要的权限，阻止
    if (!permissions.containsAll(must) && !must.isEmpty()) {
      throw new AuthException(AuthExceptionType.NOT_PERMISSION);
    }

    // 2.校验只需满足一个的权限
    List<String> or = Arrays.asList(permission.or());
    if (hasPrefix) {
      or = or.stream().map(item -> permissionPrefix.value() + item).collect(Collectors.toList());
    }
    if (!or.isEmpty()) {
      for (String s : or) {
        // 拥有一个即通过
        if (permissions.contains(s)) {
          return;
        }
      }
      throw new AuthException(AuthExceptionType.NOT_PERMISSION);
    }
  }
}
