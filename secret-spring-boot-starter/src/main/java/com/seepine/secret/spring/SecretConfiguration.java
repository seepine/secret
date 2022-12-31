package com.seepine.secret.spring;

import com.seepine.secret.impl.DefaultAuthServiceImpl;
import com.seepine.secret.interfaces.AuthService;
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
  @ConditionalOnMissingBean(AuthService.class)
  public AuthService authService() {
    return new DefaultAuthServiceImpl(authProperties);
  }
}
