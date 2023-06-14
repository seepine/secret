package com.seepine.secret.spring.init;

import com.seepine.secret.AuthUtil;
import com.seepine.secret.interfaces.TokenService;
import com.seepine.secret.spring.properties.AuthPropertiesImpl;
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
  @Resource
  TokenService authService;

  @Resource
  AuthPropertiesImpl authProperties;

  @Override
  public void run(ApplicationArguments args) {
    AuthUtil.init(authProperties, authService);
  }
}
