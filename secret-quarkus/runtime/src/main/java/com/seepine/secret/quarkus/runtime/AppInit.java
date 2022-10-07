package com.seepine.secret.quarkus.runtime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seepine.secret.AuthUtil;
import com.seepine.secret.interfaces.TokenParser;
import com.seepine.secret.properties.AuthProperties;
import com.seepine.secret.util.RedisUtil;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

import javax.enterprise.event.Observes;

/**
 * 初始化
 *
 * @author seepine
 */
public class AppInit {
  private static final String JACKSON_CODEC = "org.redisson.codec.JsonJacksonCodec";

  @ConfigProperty(name = "quarkus.redisson.codec", defaultValue = JACKSON_CODEC)
  String redissonCodec;
  /**
   * @param event event
   * @param redissonClient redis
   */
  void startup(
      @Observes StartupEvent event,
      RedissonClient redissonClient,
      ObjectMapper mapper,
      AuthProperties authProperties,
      TokenParser tokenParser) {
    if (redissonClient != null) {
      if (JACKSON_CODEC.equals(redissonCodec)) {
        Config config = redissonClient.getConfig();
        config.setCodec(new JsonJacksonCodec(mapper));
        RedisUtil.init(Redisson.create(config));
      } else {
        RedisUtil.init(redissonClient);
      }
    }
    AuthUtil.init(authProperties, tokenParser);
  }
}
