package com.seepine.secret.quarkus.runtime.properties;

import com.seepine.secret.properties.AuthProperties;
import org.eclipse.microprofile.config.inject.ConfigProperties;

import javax.inject.Singleton;

/**
 * @author seepine
 */
@Singleton
@ConfigProperties(prefix = "secret")
public class AuthPropertiesImpl extends AuthProperties {}
