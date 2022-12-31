package com.seepine.secret.quarkus.runtime;

import com.seepine.secret.AuthUtil;
import com.seepine.secret.interfaces.AuthService;
import com.seepine.secret.properties.AuthProperties;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.event.Observes;

/**
 * 初始化
 *
 * @author seepine
 */
public class AppInit {
  /**
   * @param event event
   * @param authProperties authProperties
   * @param authService authService
   */
  void startup(
      @Observes StartupEvent event, AuthProperties authProperties, AuthService authService) {
    AuthUtil.init(authProperties, authService);
  }
}
