package com.seepine.secret.spring.interceptor;

import com.seepine.secret.AuthUtil;
import com.seepine.secret.properties.AuthProperties;
import com.seepine.secret.util.TokenUtil;
import org.springframework.lang.NonNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author seepine
 */
public class AuthInterceptor implements HandlerInterceptor {
  AuthProperties authProperties;

  public AuthInterceptor(AuthProperties authProperties) {
    this.authProperties = authProperties;
  }

  @Override
  public boolean preHandle(
      @NonNull HttpServletRequest httpServletRequest,
      @NonNull HttpServletResponse httpServletResponse,
      @NonNull Object handler) {
    if (!(handler instanceof HandlerMethod)) {
      return true;
    }
    HandlerMethod handlerMethod = (HandlerMethod) handler;
    return TokenUtil.filter(
        handlerMethod.getMethod(), httpServletRequest.getHeader("Authorization"));
  }

  /**
   * clear ThreadLocal
   *
   * @param httpServletRequest httpServletRequest
   * @param httpServletResponse httpServletResponse
   * @param o o
   * @param e e
   */
  @Override
  public void afterCompletion(
      @NonNull HttpServletRequest httpServletRequest,
      @NonNull HttpServletResponse httpServletResponse,
      @NonNull Object o,
      Exception e) {
    AuthUtil.clear();
  }
}
