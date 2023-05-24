package com.seepine.secret.quarkus.runtime.config;

import com.seepine.secret.impl.DefaultPermissionServiceImpl;
import com.seepine.secret.impl.DefaultTokenServiceImpl;
import com.seepine.secret.interfaces.PermissionService;
import com.seepine.secret.interfaces.TokenService;
import com.seepine.secret.properties.AuthProperties;
import io.quarkus.arc.DefaultBean;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Produces;


/**
 * @author seepine
 */
public class SecretConfig {
	@Inject
	AuthProperties authProperties;

	@Produces
	@DefaultBean
	@Singleton
	public TokenService tokenService() {
		return new DefaultTokenServiceImpl(authProperties);
	}

	@Produces
	@DefaultBean
	@Singleton
	public PermissionService permissionService() {
		return new DefaultPermissionServiceImpl(authProperties);
	}
}

