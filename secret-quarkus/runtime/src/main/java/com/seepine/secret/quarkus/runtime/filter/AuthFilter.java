package com.seepine.secret.quarkus.runtime.filter;

import com.seepine.secret.AuthUtil;
import com.seepine.secret.util.TokenUtil;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.*;
import jakarta.ws.rs.ext.Provider;

import java.lang.reflect.Method;

/**
 * 鉴权过滤器
 *
 * @author seepine
 */
@Priority(Integer.MIN_VALUE + 10)
@Provider
public class AuthFilter implements ContainerRequestFilter, ContainerResponseFilter {
	@Inject
	ResourceInfo resourceInfo;

	@Override
	public void filter(ContainerRequestContext containerRequestContext) {
		Method method = resourceInfo.getResourceMethod();
		TokenUtil.filter(method, containerRequestContext.getHeaderString("Authorization"));
	}

	@Override
	public void filter(
		ContainerRequestContext containerRequestContext,
		ContainerResponseContext containerResponseContext) {
		AuthUtil.clear();
	}
}
