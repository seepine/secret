package com.seepine.secret.util;

import com.seepine.secret.AuthUtil;
import com.seepine.secret.annotation.Expose;
import com.seepine.secret.annotation.NotExpose;
import com.seepine.secret.enums.AuthExceptionType;
import com.seepine.secret.exception.AuthException;
import com.seepine.tool.util.StrUtil;

import java.lang.reflect.Method;

public class TokenUtil {
  public static boolean filter(Method method, String token) {
    if (StrUtil.isNotEmpty(token) && token.startsWith("Bearer ")) {
      token = token.substring(7);
    }
    boolean isFindAndFill = AuthUtil.findAndFill(token);
    // not @NotExpose and has @Expose, pass
    if (!AnnotationUtil.hasAnnotation(method, NotExpose.class)
        && AnnotationUtil.hasAnnotation(method, Expose.class)) {
      return true;
    }
    // token is blank, Not Acceptable
    if (StrUtil.isBlank(token)) {
      throw new AuthException(AuthExceptionType.NOT_TOKEN);
    }
    // has token but can not get user info, Unauthorized
    if (!isFindAndFill) {
      throw new AuthException(AuthExceptionType.INVALID_TOKEN);
    }
    return true;
  }
}
