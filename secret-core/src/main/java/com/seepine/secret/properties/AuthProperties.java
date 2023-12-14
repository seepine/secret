package com.seepine.secret.properties;

/**
 * @author seepine
 */
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

  public Long getExpires() {
    return expires;
  }

  public void setExpires(Long expires) {
    this.expires = expires;
  }

  public Long getRefreshExpires() {
    return refreshExpires;
  }

  public void setRefreshExpires(Long refreshExpires) {
    this.refreshExpires = refreshExpires;
  }

  public String getCachePrefix() {
    return cachePrefix;
  }

  public void setCachePrefix(String cachePrefix) {
    this.cachePrefix = cachePrefix;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public String[] getExcludePathPatterns() {
    return excludePathPatterns;
  }

  public void setExcludePathPatterns(String[] excludePathPatterns) {
    this.excludePathPatterns = excludePathPatterns;
  }

  public String[] getDefaultExcludePathPatterns() {
    return defaultExcludePathPatterns;
  }

  public void setDefaultExcludePathPatterns(String[] defaultExcludePathPatterns) {
    this.defaultExcludePathPatterns = defaultExcludePathPatterns;
  }

  public String getIssuer() {
    return issuer;
  }

  public void setIssuer(String issuer) {
    this.issuer = issuer;
  }
}
