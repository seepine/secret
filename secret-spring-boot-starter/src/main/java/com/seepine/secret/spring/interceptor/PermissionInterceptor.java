package com.seepine.secret.spring.interceptor;

import com.seepine.secret.annotation.Permission;
import com.seepine.secret.annotation.PermissionPrefix;
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
 * @since 2.0.0
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
    Permission permission = PermissionUtil.getPermission(method);
    if (permission != null) {
      PermissionPrefix prefix = handlerMethod.getBeanType().getAnnotation(PermissionPrefix.class);
      PermissionUtil.verify(
          permission,
          prefix == null
              ? method.getDeclaringClass().getAnnotation(PermissionPrefix.class)
              : prefix);
    }
    return true;
  }
}
