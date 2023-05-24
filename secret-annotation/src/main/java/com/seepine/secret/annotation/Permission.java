package com.seepine.secret.annotation;

import java.lang.annotation.*;

/**
 * 鉴权，作用于方法上
 *
 * @author seepine
 */
@Documented
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {
	/**
	 * 必须包含所有权限才能通过
	 *
	 * @return [permission1, permission2...]
	 */
	String[] value() default {};

	/**
	 * 满足任意一个权限即可通过
	 *
	 * @return [permission1, permission2...]
	 */
	String[] or() default {};
}
