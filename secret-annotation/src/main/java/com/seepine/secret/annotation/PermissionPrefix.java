package com.seepine.secret.annotation;

import java.lang.annotation.*;

/**
 * 为类中所有@Permission加上前缀，作用于类上
 *
 * @author seepine
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionPrefix {
  String value() default "";
}
