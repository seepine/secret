package com.seepine.secret.spring.interceptor;

import com.seepine.secret.AuthUtil;
import com.seepine.secret.annotation.Permission;
import com.seepine.secret.annotation.Role;
import com.seepine.secret.util.AnnotationUtil;
import com.seepine.secret.util.PermissionUtil;
import org.springframework.lang.NonNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * PermissionInterceptor
 *
 * @author seepine
 * @since 1.0.0
 */
public class PermissionInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(
    @NonNull HttpServletRequest httpServletRequest,
    @NonNull HttpServletResponse httpServletResponse,
    @NonNull Object handler) {
    if (!(handler instanceof HandlerMethod)) {
      return true;
    }
    HandlerMethod handlerMethod = (HandlerMethod) handler;
    Method method = handlerMethod.getMethod();
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
    return true;
  }
}
