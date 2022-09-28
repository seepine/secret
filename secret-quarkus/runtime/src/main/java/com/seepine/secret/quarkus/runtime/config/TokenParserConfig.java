package com.seepine.secret.quarkus.runtime.config;

import com.seepine.secret.impl.RedisTokenParser;
import com.seepine.secret.interfaces.TokenParser;
import com.seepine.secret.properties.AuthProperties;
import io.quarkus.arc.DefaultBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Produces;

/**
 * @author seepine
 */
public class TokenParserConfig {
  @Inject AuthProperties authProperties;

  @Produces
  @DefaultBean
  @ApplicationScoped
  public TokenParser tokenParser() {
    return new RedisTokenParser(authProperties);
  }
}
