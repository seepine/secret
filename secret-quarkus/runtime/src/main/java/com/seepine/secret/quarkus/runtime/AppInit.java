package com.seepine.secret.quarkus.runtime;

import com.seepine.secret.AuthUtil;
import com.seepine.secret.interfaces.TokenParser;
import com.seepine.secret.util.RedisUtil;
import io.quarkus.runtime.StartupEvent;
import org.redisson.api.RedissonClient;

import javax.enterprise.event.Observes;

/**
 * 初始化
 *
 * @author seepine
 */
public class AppInit {

  /**
   * @param event event
   * @param redissonClient redis
   */
  void startup(
      @Observes StartupEvent event, RedissonClient redissonClient, TokenParser tokenParser) {
    if (redissonClient != null) {
      RedisUtil.init(redissonClient);
    }
    AuthUtil.init(tokenParser);
  }
}
