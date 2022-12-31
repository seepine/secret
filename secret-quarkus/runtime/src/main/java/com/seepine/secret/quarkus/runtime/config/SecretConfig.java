package com.seepine.secret.quarkus.runtime.config;

import com.seepine.secret.impl.DefaultAuthServiceImpl;
import com.seepine.secret.interfaces.AuthService;
import com.seepine.secret.properties.AuthProperties;
import io.quarkus.arc.DefaultBean;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author seepine
 */
public class SecretConfig {
  @Inject AuthProperties authProperties;

  @Produces
  @DefaultBean
  @Singleton
  public AuthService authService() {
    return new DefaultAuthServiceImpl(authProperties);
  }
}
