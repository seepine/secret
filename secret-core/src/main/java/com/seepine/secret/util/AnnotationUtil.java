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
   * @param annotationClass annotationClass
   * @return boolean
   */
  public static boolean hasAnnotation(Method method, Class<? extends Annotation> annotationClass) {
    try {
      return method.isAnnotationPresent(annotationClass)
          || method.getDeclaringClass().isAnnotationPresent(annotationClass);
    } catch (Exception ignored) {
      return false;
    }
  }
}
