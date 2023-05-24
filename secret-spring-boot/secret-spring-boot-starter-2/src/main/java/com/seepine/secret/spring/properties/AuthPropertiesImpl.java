package com.seepine.secret.spring.properties;

import com.seepine.secret.properties.AuthProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author seepine
 */
@ConfigurationProperties(prefix = "secret")
public class AuthPropertiesImpl extends AuthProperties {}
