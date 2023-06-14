package com.seepine.secret.properties;

/**
 * @author seepine
 */
public class AuthProperties {
  /** token过期时间，单位秒，默认12小时 */
  private Long expiresSecond = 12 * 60 * 60L;
  /** 缓存前缀 */
  private String cachePrefix = "com.seepine.secret:";
  /** 默认生成token所用aes密钥 */
  private String secret = "comseepinesecret";
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

  public Long getExpiresSecond() {
    return expiresSecond;
  }

  public void setExpiresSecond(Long expiresSecond) {
    this.expiresSecond = expiresSecond;
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
}
