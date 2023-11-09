package com.seepine.secret.quarkus.runtime;

import com.seepine.secret.AuthUtil;
import com.seepine.secret.interfaces.BanService;
import com.seepine.secret.interfaces.PermissionService;
import com.seepine.secret.interfaces.TokenService;
import com.seepine.secret.properties.AuthProperties;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;

/**
 * 初始化
 *
 * @author seepine
 */
public class AppInit {
  /**
   * @param event event
   * @param authProperties authProperties
   * @param tokenService tokenService
   * @param banService banService
   */
  void startup(
      @Observes StartupEvent event,
      AuthProperties authProperties,
      TokenService tokenService,
      PermissionService permissionService,
      BanService banService) {
    AuthUtil.init(authProperties, tokenService, permissionService, banService);
  }
}
