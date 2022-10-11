package com.seepine.secret.spring;

import com.seepine.secret.impl.DefaultAuthCacheImpl;
import com.seepine.secret.impl.DefaultAuthTokenGenImpl;
import com.seepine.secret.interfaces.AuthCache;
import com.seepine.secret.interfaces.AuthTokenGen;
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
  @ConditionalOnMissingBean(AuthTokenGen.class)
  public AuthTokenGen authTokenGen() {
    return new DefaultAuthTokenGenImpl(authProperties);
  }

  @Bean
  @ConditionalOnMissingBean(AuthTokenGen.class)
  public AuthCache authCache() {
    return new DefaultAuthCacheImpl(authProperties);
  }
}
