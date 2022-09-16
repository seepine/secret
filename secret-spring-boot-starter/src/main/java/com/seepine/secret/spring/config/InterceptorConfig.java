package com.seepine.secret.spring.config;

import com.seepine.secret.spring.interceptor.AuthInterceptor;
import com.seepine.secret.spring.interceptor.PermissionInterceptor;
import com.seepine.secret.spring.properties.AuthPropertiesImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author seepine
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
  @Resource private AuthPropertiesImpl authProperties;

  @Override
  public void addInterceptors(@NonNull InterceptorRegistry registry) {
    // 拦截非@Expose注解
    if (authProperties.getEnabled()) {
      registry
          .addInterceptor(new AuthInterceptor(authProperties))
          .addPathPatterns("/**")
          .excludePathPatterns(authProperties.getDefaultExcludePathPatterns())
          .excludePathPatterns(authProperties.getExcludePathPatterns())
          .order(authProperties.getInterceptorOrder());
    }
    // 拦截@Permission注解
    registry
        .addInterceptor(new PermissionInterceptor())
        .addPathPatterns("/**")
        .order(authProperties.getInterceptorOrder() + 100);
  }
}
