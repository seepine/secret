package com.seepine.secret.quarkus.runtime.filter;

import com.seepine.secret.AuthUtil;
import com.seepine.secret.annotation.Permission;
import com.seepine.secret.annotation.Role;
import com.seepine.secret.util.AnnotationUtil;
import com.seepine.secret.util.PermissionUtil;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.ext.Provider;

import java.lang.reflect.Method;

/**
 * 权限过滤器，优先值比auth低
 *
 * @author seepine
 */
@Priority(Integer.MIN_VALUE + 100)
@Provider
public class PermissionFilter implements ContainerRequestFilter {
  @Inject
  ResourceInfo resourceInfo;

  @Override
  public void filter(ContainerRequestContext containerRequestContext) {
    Method method = resourceInfo.getResourceMethod();
    // 校验角色
    Role role = AnnotationUtil.getAnnotation(method, Role.class);
    if (role != null) {
      PermissionUtil.verify(AuthUtil.getRoles(), role.value(), role.or());
    }
    // 校验权限
    Permission permission = AnnotationUtil.getAnnotation(method, Permission.class);
    if (permission != null) {
      PermissionUtil.verify(AuthUtil.getPermissions(), permission.value(), permission.or());
    }
  }
}
