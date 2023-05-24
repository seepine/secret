package com.seepine.secret.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author seepine
 */
public class AnnotationUtil {

	/**
	 * 方法上有注解或类上有注解
	 *
	 * @param method method
	 * @param tClass tClass
	 * @param <T>    T extends Annotation
	 * @return boolean
	 */
	public static <T extends Annotation> boolean hasAnnotation(Method method, Class<T> tClass) {
		try {
			return getAnnotation(method, tClass) != null;
		} catch (Exception ignored) {
			return false;
		}
	}

	/**
	 * 获取注解
	 *
	 * @param method method
	 * @param tClass tClass
	 * @param <T>    T extends Annotation
	 * @return 注解
	 * @since 1.0.0
	 */
	public static <T extends Annotation> T getAnnotation(Method method, Class<T> tClass) {
		T annotation = null;
		if (method.isAnnotationPresent(tClass)) {
			annotation = method.getAnnotation(tClass);
		} else {
			try {
				// 1.判断是否重写父类方法
				Method parentMethod =
					method.getDeclaringClass().getSuperclass().getDeclaredMethod(method.getName());
				// 2.是的话判断父类的方法是否有注解
				if (parentMethod.isAnnotationPresent(tClass)) {
					annotation = parentMethod.getAnnotation(tClass);
				}
			} catch (NoSuchMethodException ignored) {
			}
		}
		return annotation;
	}
}
