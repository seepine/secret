package com.seepine.secret.spring.config;

import com.seepine.secret.spring.interceptor.AuthInterceptor;
import com.seepine.secret.spring.interceptor.PermissionInterceptor;
import com.seepine.secret.spring.properties.AuthPropertiesImpl;
import javax.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author seepine
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
  @Resource private AuthPropertiesImpl authProperties;

  @Override
  public void addInterceptors(@NonNull InterceptorRegistry registry) {
    // 拦截鉴权
    registry
        .addInterceptor(new AuthInterceptor())
        .addPathPatterns("/**")
        .excludePathPatterns(authProperties.getDefaultExcludePathPatterns())
        .excludePathPatterns(authProperties.getExcludePathPatterns())
        .order(Integer.MIN_VALUE + 10);
    // 拦截权限
    registry
        .addInterceptor(new PermissionInterceptor())
        .addPathPatterns("/**")
        .order(Integer.MIN_VALUE + 100);
  }
}
