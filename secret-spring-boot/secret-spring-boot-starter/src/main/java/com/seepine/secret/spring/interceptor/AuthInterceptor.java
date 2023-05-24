package com.seepine.secret.spring.interceptor;

import com.seepine.secret.AuthUtil;
import com.seepine.secret.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author seepine
 */
public class AuthInterceptor implements HandlerInterceptor {

  public AuthInterceptor() {}

  @Override
  public boolean preHandle(
      @NonNull HttpServletRequest httpServletRequest,
      @NonNull HttpServletResponse httpServletResponse,
      @NonNull Object handler) {
    if (!(handler instanceof HandlerMethod handlerMethod)) {
      return true;
    }
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
