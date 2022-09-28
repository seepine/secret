package com.seepine.secret.properties;

import lombok.Data;
/**
 * @author seepine
 */
@Data
public class AuthProperties {
  /** token过期时间，单位秒，默认24小时 */
  Long timeout = 24 * 60 * 60L;
  /** 默认aes加密密钥，16/24/32 */
  String secret = "comseepinesecret";
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
