package com.seepine.secret.quarkus.runtime.config;

import com.seepine.secret.properties.AuthProperties;
import com.seepine.secret.quarkus.runtime.properties.SecretProperties;
import io.quarkus.arc.DefaultBean;
import org.eclipse.microprofile.config.inject.ConfigProperties;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author seepine
 */
public class AutoConfig {
  @Inject @ConfigProperties SecretProperties secretProperties;

  @Produces
  @DefaultBean
  @Singleton
  public AuthProperties authProperties() {
    AuthProperties authProperties = new AuthProperties();
    authProperties.setExpiresAt(secretProperties.expiresAt);
    authProperties.setSecret(secretProperties.secret);
    authProperties.setCachePrefix(secretProperties.cachePrefix);
    authProperties.setIssuer(secretProperties.issuer);
    return authProperties;
  }
}
