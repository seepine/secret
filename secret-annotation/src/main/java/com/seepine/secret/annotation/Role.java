package com.seepine.secret.annotation;

import java.lang.annotation.*;

/**
 * 鉴权，作用于方法上
 *
 * @author seepine
 * @since 1.0.0
 */
@Documented
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Role {
	/**
	 * 必须包含所有角色才能通过
	 *
	 * @return [role1, role2...]
	 */
	String[] value() default {};

	/**
	 * 满足任意一个角色即可通过
	 *
	 * @return [role1, role2...]
	 */
	String[] or() default {};
}
