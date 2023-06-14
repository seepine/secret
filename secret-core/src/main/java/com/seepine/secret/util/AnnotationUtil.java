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
      T annotation = getAnnotation(method, tClass);
      return annotation != null;
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
    if (method == null || tClass == null) {
      return null;
    }
    // 判断方法上是否有注解
    if (method.isAnnotationPresent(tClass)) {
      return method.getAnnotation(tClass);
    }
    // 判断类上是否有注解
    if (method.getDeclaringClass().isAnnotationPresent(tClass)) {
      return method.getDeclaringClass().getAnnotation(tClass);
    }
    try {
      // 2.判断是否有继承
      Method parentMethod =
        method.getDeclaringClass().getSuperclass().getDeclaredMethod(method.getName());
      return getAnnotation(parentMethod, tClass);
    } catch (NoSuchMethodException ignored) {
    }
    return null;
  }
}
