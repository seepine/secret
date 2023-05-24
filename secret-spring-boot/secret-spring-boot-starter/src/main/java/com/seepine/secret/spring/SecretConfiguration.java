package com.seepine.secret.spring;

import com.seepine.secret.impl.DefaultPermissionServiceImpl;
import com.seepine.secret.impl.DefaultTokenServiceImpl;
import com.seepine.secret.interfaces.PermissionService;
import com.seepine.secret.interfaces.TokenService;
import com.seepine.secret.spring.properties.AuthPropertiesImpl;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author seepine
 */
@Configuration
@EnableConfigurationProperties({AuthPropertiesImpl.class})
public class SecretConfiguration {
  @Resource private AuthPropertiesImpl authProperties;

  /**
   * 填充authService
   *
   * @return DefaultTokenServiceImpl
   */
  @Bean
  @ConditionalOnMissingBean(TokenService.class)
  public TokenService authService() {
    return new DefaultTokenServiceImpl(authProperties);
  }

  /**
   * 填充permissionService
   *
   * @return DefaultPermissionServiceImpl
   */
  @Bean
  @ConditionalOnMissingBean(PermissionService.class)
  public PermissionService permissionService() {
    return new DefaultPermissionServiceImpl(authProperties);
  }
}
