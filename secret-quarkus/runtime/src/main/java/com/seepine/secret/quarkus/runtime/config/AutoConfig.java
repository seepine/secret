package com.seepine.secret.quarkus.runtime.config;

import com.seepine.secret.properties.AuthProperties;
import io.quarkus.arc.DefaultBean;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
/**
 * @author seepine
 */
public class AutoConfig {
  @Produces
  @DefaultBean
  @Singleton
  public AuthProperties authProperties() {
    return new AuthProperties();
  }
}
