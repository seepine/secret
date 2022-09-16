package com.seepine.secret.quarkus.runtime.filter;

import com.seepine.secret.util.TokenUtil;
import io.quarkus.arc.Priority;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;

@Priority(Integer.MIN_VALUE)
@Provider
public class AuthFilter implements ContainerRequestFilter {
  @Inject ResourceInfo resourceInfo;

  @Override
  public void filter(ContainerRequestContext containerRequestContext) {
    Method method = resourceInfo.getResourceMethod();
    TokenUtil.filter(method, containerRequestContext.getHeaderString("Authorization"));
  }
}
