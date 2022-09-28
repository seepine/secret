package com.seepine.secret.spring;

import com.seepine.secret.impl.RedisTokenParser;
import com.seepine.secret.interfaces.TokenParser;
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
public class AutoConfiguration {
  @Resource private AuthPropertiesImpl authProperties;

  @Bean
  @ConditionalOnMissingBean(TokenParser.class)
  public TokenParser tokenParser() {
    return new RedisTokenParser(authProperties);
  }
}
