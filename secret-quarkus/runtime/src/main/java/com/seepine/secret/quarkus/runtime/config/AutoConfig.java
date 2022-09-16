package com.seepine.secret.quarkus.runtime.config;

import com.seepine.secret.properties.AuthProperties;
import io.quarkus.arc.DefaultBean;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Produces;

public class AutoConfig {
  @Produces
  @DefaultBean
  @ApplicationScoped
  public AuthProperties authProperties() {
    return new AuthProperties();
  }
}
