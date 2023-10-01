package com.seepine.secret.quarkus.runtime.properties;

import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * @author seepine
 */
@ConfigProperties(prefix = "secret")
public class SecretProperties {
  /** token过期时间，单位秒，默认12小时 */
  @ConfigProperty(name = "expires-second", defaultValue = "43200")
  public Long expiresSecond;
  /** 缓存前缀 */
  @ConfigProperty(name = "cache-prefix", defaultValue = "com.seepine.secret")
  public String cachePrefix;
  /** 默认生成token所用密钥 */
  @ConfigProperty(defaultValue = "comseepinesecret")
  public String secret;
}
