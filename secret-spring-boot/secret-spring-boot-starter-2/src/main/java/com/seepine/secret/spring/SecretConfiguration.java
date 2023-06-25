package com.seepine.secret.spring;

import com.seepine.secret.impl.DefaultBanServiceImpl;
import com.seepine.secret.impl.DefaultTokenServiceImpl;
import com.seepine.secret.interfaces.BanService;
import com.seepine.secret.interfaces.TokenService;
import com.seepine.secret.spring.properties.AuthPropertiesImpl;
import javax.annotation.Resource;
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
   * 填充tokenService
   *
   * @return DefaultTokenServiceImpl
   */
  @Bean
  @ConditionalOnMissingBean(TokenService.class)
  public TokenService tokenService() {
    return new DefaultTokenServiceImpl(authProperties);
  }
  /**
   * 填充banService
   *
   * @return DefaultBanServiceImpl
   */
  @Bean
  @ConditionalOnMissingBean(BanService.class)
  public BanService banService() {
    return new DefaultBanServiceImpl(authProperties);
  }
}
