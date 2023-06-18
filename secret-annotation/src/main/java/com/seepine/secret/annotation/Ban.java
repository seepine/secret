package com.seepine.secret.annotation;

import java.lang.annotation.*;

/**
 * 禁令，作用于方法上
 *
 * @code @Ban("comment")，表示若用户被ban了comment功能，则无法访问
 * @code AuthUtil.ban("comment")，主动ban某项功能
 * @author seepine
 * @since 1.0.0
 */
@Documented
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Ban {
  /**
   * 若相应功能被ban，则不能访问，默认没有被ban
   *
   * @return ['comment','pay']
   */
  String[] value() default {};
}
