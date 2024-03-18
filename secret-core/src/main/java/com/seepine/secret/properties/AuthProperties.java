package com.seepine.secret.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author seepine
 */
@Getter
@Setter
public class AuthProperties {
  /** token过期时间，单位秒，默认12小时 */
  private Long expires = 12 * 60 * 60L;

  /** refreshToken过期时间，单位秒，默认48小时 */
  private Long refreshExpires = 2 * 24 * 60 * 60L;

  /** 缓存前缀 */
  private String cachePrefix = "com.seepine.secret";

  /** 默认生成token所用密钥，请务必修改此值 */
  private String secret = "comseepinesecret";

  /** jwt颁发者 */
  private String issuer = "https://secret.seepine.com";

  /** 拦截器排除的pathPatterns */
  private String[] excludePathPatterns = new String[] {};

  /** 拦截器默认排除的pathPatterns */
  private String[] defaultExcludePathPatterns =
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
