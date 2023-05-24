package com.seepine.secret.spring.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author seepine
 */
public class HttpServletUtil {
  /**
   * 获取request
   *
   * @return HttpServletRequest | null
   */
  public static HttpServletRequest getHttpRequest() {
    if (RequestContextHolder.getRequestAttributes() == null) {
      return null;
    }
    return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
  }
}
