package com.seepine.secret.util;

import com.seepine.secret.exception.ForbiddenSecretException;

import java.util.Arrays;
import java.util.Set;

/**
 * @author seepine
 * @since 1.0.0
 */
public class PermissionUtil {

  /**
   * 校验角色或权限
   *
   * @param must        满足所有
   * @param or          满足任意一个
   * @param permissions 权限或角色集合
   * @throws ForbiddenSecretException 未授权
   */
  public static void verify(Set<String> permissions, String[] must, String[] or)
    throws ForbiddenSecretException {
    if (must == null && or == null) {
      return;
    }
    if (must == null) {
      verifyOr(permissions, or);
    } else if (or == null) {
      verifyMust(permissions, must);
    } else {
      verifyMust(permissions, must);
      verifyOr(permissions, or);
    }
  }

  public static void verifyMust(Set<String> permissions, String[] must) {
    // 1.校验必须包含的权限
    if (must == null) {
      return;
    }
    for (String s : must) {
      // 如果权限不为空，并且没有拥有所有需要的权限，阻止
      if (!permissions.contains(s)) {
        throw new ForbiddenSecretException("Not granted: " + s);
      }
    }
  }

  public static void verifyOr(Set<String> permissions, String[] or) {
    // 2.校验只需满足一个的权限
    if (or == null) {
      return;
    }
    for (String s : or) {
      // 拥有一个即通过
      if (permissions.contains(s)) {
        return;
      }
    }
    throw new ForbiddenSecretException("At least one is required: " + Arrays.toString(or));
  }
}
