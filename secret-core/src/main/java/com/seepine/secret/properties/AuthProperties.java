package com.seepine.secret.properties;

import lombok.Data;
/**
 * @author seepine
 */
@Data
public class AuthProperties {
  /** token过期时间，单位秒，默认72小时 */
  Long expiresAt = 3 * 24 * 60 * 60L;
  /** 缓存前缀 */
  String cachePrefix = "com.seepine.secret:";
  /** 默认jwt加密密钥 */
  String secret = "com.seepine.secret";
  /** 默认jwt的issuer */
  String issuer = "secret";

  /** 拦截器排除的pathPatterns */
  String[] excludePathPatterns = new String[] {};
  /** 拦截器默认排除的pathPatterns */
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
}
