package com.seepine.secret.annotation;

import java.lang.annotation.*;

/**
 * 直接暴露接口，不鉴权
 *
 * @author seepine
 */
@Documented
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Expose {
  /**
   * 是否跳过token检测，即就算请求附带token，也不会去解析token信息
   *
   * @return bool
   */
  boolean skip() default false;
}
