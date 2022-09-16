package com.seepine.secret.quarkus.runtime;

import com.seepine.secret.AuthUtil;
import com.seepine.secret.impl.DefaultRedisCache;
import com.seepine.secret.impl.DefaultSnowflakeTokenGenerator;
import com.seepine.secret.properties.AuthProperties;
import com.seepine.secret.util.RedisUtil;
import io.quarkus.runtime.StartupEvent;
import org.redisson.api.RedissonClient;

import javax.enterprise.event.Observes;

public class AppInit {

  /**
   * @param event event
   * @param redissonClient redis
   */
  void startup(
      @Observes StartupEvent event, RedissonClient redissonClient, AuthProperties authProperties) {
    AuthUtil.setProperties(authProperties);
    if (redissonClient != null) {
      RedisUtil.init(redissonClient);
      AuthUtil.setTokenGenerator(new DefaultSnowflakeTokenGenerator());
      AuthUtil.setAuthCache(new DefaultRedisCache());
    }
  }
}
