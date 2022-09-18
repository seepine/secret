package com.seepine.secret.properties;

import lombok.Data;
/**
 * @author seepine
 */
@Data
public class AuthProperties {
  /** 是否开启接口加密 */
  Boolean enabled = Boolean.TRUE;
  /** 缓存前缀 */
  String cachePrefix = "com.seepine.secret";
  /** 是否自动重置登录过期时间 */
  Boolean resetTimeout = Boolean.TRUE;
  /** 过期时间，默认24小时 */
  Long timeout = 24 * 60 * 60L;
  /** 拦截器排除的pathPatterns */
  String[] excludePathPatterns = new String[] {};
  /** 拦截器排除的pathPatterns */
  String[] defaultExcludePathPatterns =
      new String[] {
        "/health",
        "/error",
        "/favicon.ico",
        "/captcha/**",
        // swagger3
        "/swagger-resources/**",
        "/v3/api-docs",
        "/webjars/**",
        "/swagger-ui.html"
      };
  /** 拦截器的order */
  Integer interceptorOrder = Integer.MIN_VALUE;
}
