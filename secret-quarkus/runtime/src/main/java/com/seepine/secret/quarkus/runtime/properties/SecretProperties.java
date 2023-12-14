package com.seepine.secret.quarkus.runtime.properties;

import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * @author seepine
 */
@ConfigProperties(prefix = "secret")
public class SecretProperties {
  /** token过期时间，单位秒，默认12小时 */
  @ConfigProperty(name = "expires", defaultValue = "43200")
  public Long expires;

  /** refresh token过期时间，单位秒，默认12小时 */
  @ConfigProperty(name = "refresh-expires", defaultValue = "172800")
  public Long refreshExpires;

  /** 缓存前缀 */
  @ConfigProperty(name = "cache-prefix", defaultValue = "com.seepine.secret")
  public String cachePrefix;

  /** 默认生成token所用密钥 */
  @ConfigProperty(defaultValue = "comseepinesecret")
  public String secret;

  /** jwt颁发者 */
  @ConfigProperty(defaultValue = "https://secret.seepine.com")
  public String issuer;
}
