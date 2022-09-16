package com.seepine.secret.spring;

import com.seepine.secret.impl.DefaultRedisCache;
import com.seepine.secret.impl.DefaultSnowflakeTokenGenerator;
import com.seepine.secret.interfaces.Cache;
import com.seepine.secret.interfaces.TokenGenerator;
import com.seepine.secret.spring.properties.AuthPropertiesImpl;
import com.seepine.secret.util.RedisUtil;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author seepine
 */
@Configuration
@EnableConfigurationProperties({AuthPropertiesImpl.class})
public class AutoConfiguration {
  @Resource RedissonClient redissonClient;

  @Bean
  @ConditionalOnMissingBean(Cache.class)
  public Cache authCache() {
    return new DefaultRedisCache();
  }

  @Bean
  @ConditionalOnMissingBean(TokenGenerator.class)
  public TokenGenerator tokenGenerator() {
    RedisUtil.init(redissonClient);
    return new DefaultSnowflakeTokenGenerator();
  }
}
