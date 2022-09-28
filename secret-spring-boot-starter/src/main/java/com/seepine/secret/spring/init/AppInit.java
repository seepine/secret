package com.seepine.secret.spring.init;

import com.seepine.secret.AuthUtil;
import com.seepine.secret.interfaces.TokenParser;
import com.seepine.secret.spring.properties.AuthPropertiesImpl;
import com.seepine.secret.util.RedisUtil;
import org.redisson.api.RedissonClient;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
/**
 * 初始化
 *
 * @author seepine
 */
@Component
public class AppInit implements ApplicationRunner {
  @Resource RedissonClient redissonClient;
  @Resource TokenParser tokenParser;
  @Resource AuthPropertiesImpl authProperties;

  @Override
  public void run(ApplicationArguments args) {
    RedisUtil.init(redissonClient);
    AuthUtil.init(authProperties, tokenParser);
  }
}
