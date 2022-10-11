package com.seepine.secret.quarkus.runtime.config;

import com.seepine.secret.impl.DefaultAuthCacheImpl;
import com.seepine.secret.impl.DefaultAuthTokenGenImpl;
import com.seepine.secret.interfaces.AuthCache;
import com.seepine.secret.interfaces.AuthTokenGen;
import com.seepine.secret.properties.AuthProperties;
import io.quarkus.arc.DefaultBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Produces;

/**
 * @author seepine
 */
public class SecretConfig {
  @Inject AuthProperties authProperties;

  @Produces
  @DefaultBean
  @ApplicationScoped
  public AuthTokenGen authTokenGen() {
    return new DefaultAuthTokenGenImpl(authProperties);
  }

  @Produces
  @DefaultBean
  @ApplicationScoped
  public AuthCache authCache() {
    return new DefaultAuthCacheImpl(authProperties);
  }
}
