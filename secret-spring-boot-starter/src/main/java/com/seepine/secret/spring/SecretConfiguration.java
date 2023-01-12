package com.seepine.secret.spring;

import com.seepine.secret.impl.DefaultCacheServiceImpl;
import com.seepine.secret.impl.DefaultTokenServiceImpl;
import com.seepine.secret.interfaces.CacheService;
import com.seepine.secret.interfaces.TokenService;
import com.seepine.secret.spring.properties.AuthPropertiesImpl;
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
public class SecretConfiguration {
  @Resource private AuthPropertiesImpl authProperties;

  @Bean
  @ConditionalOnMissingBean(TokenService.class)
  public TokenService authService() {
    return new DefaultTokenServiceImpl(authProperties);
  }

  @Bean
  @ConditionalOnMissingBean(CacheService.class)
  public CacheService cacheService() {
    return new DefaultCacheServiceImpl();
  }
}
