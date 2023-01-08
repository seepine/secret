package com.seepine.secret.quarkus.runtime.config;

import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.properties.AuthProperties;
import com.seepine.tool.R;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection(targets = {AuthUser.class, AuthProperties.class, R.class})
public class SecretReflectionConfig {}
