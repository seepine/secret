package com.seepine.secret.quarkus.runtime.config;

import com.seepine.secret.properties.AuthProperties;
import com.seepine.secret.quarkus.runtime.properties.SecretProperties;
import io.quarkus.arc.DefaultBean;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperties;


/**
 * @author seepine
 */
public class AutoConfig {
	@Inject
	@ConfigProperties
	SecretProperties secretProperties;

	@Produces
	@DefaultBean
	@Singleton
	public AuthProperties authProperties() {
		AuthProperties authProperties = new AuthProperties();
		authProperties.setExpiresSecond(secretProperties.expiresSecond);
		authProperties.setSecret(secretProperties.secret);
		authProperties.setCachePrefix(secretProperties.cachePrefix);
		return authProperties;
	}
}
