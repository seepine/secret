package com.seepine.secret.quarkus.runtime.properties;

import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * @author seepine
 */
@ConfigProperties(prefix = "secret")
public class SecretProperties {
  /** token过期时间，单位秒，默认12小时 */
  @ConfigProperty(name = "expires-at", defaultValue = "43200")
  public Long expiresAt;
  /** 缓存前缀 */
  @ConfigProperty(name = "cache-prefix", defaultValue = "com.seepine.secret:")
  public String cachePrefix;
  /** 默认jwt加密密钥 */
  @ConfigProperty(defaultValue = "com.seepine.secret")
  public String secret;
  /** 默认jwt的issuer */
  @ConfigProperty(defaultValue = "secret")
  public String issuer;
  /** 默认jwt的issuer */
  @ConfigProperty(name = "enable-whitelist", defaultValue = "false")
  public Boolean enableWhitelist;
}
