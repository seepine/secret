package com.seepine.secret.quarkus.runtime.filter;

import com.seepine.secret.util.PermissionUtil;
import io.quarkus.arc.Priority;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;

/**
 * 权限过滤器，优先值比auth低
 *
 * @author seepine
 */
@Priority(Integer.MIN_VALUE + 100)
@Provider
public class PermissionFilter implements ContainerRequestFilter {
  @Inject ResourceInfo resourceInfo;

  @Override
  public void filter(ContainerRequestContext containerRequestContext) {
    Method method = resourceInfo.getResourceMethod();
    PermissionUtil.verify(method);
  }
}
