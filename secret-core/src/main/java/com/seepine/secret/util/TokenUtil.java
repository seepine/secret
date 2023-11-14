package com.seepine.secret.util;

import com.seepine.secret.AuthUtil;
import com.seepine.secret.annotation.Expose;
import com.seepine.secret.annotation.NotExpose;
import com.seepine.secret.exception.UnauthorizedSecretException;
import com.seepine.tool.util.Objects;

import java.lang.reflect.Method;

/**
 * token 判断逻辑
 *
 * @author seepine
 */
public class TokenUtil {
  private static final String HEADER_PREFIX = "Bearer ";

  public static boolean filter(Method method, String token) throws UnauthorizedSecretException {
    if (Objects.nonEmpty(token) && token.startsWith(HEADER_PREFIX)) {
      token = token.substring(7);
    }
    Expose expose = AnnotationUtil.getAnnotation(method, Expose.class);
    // not @NotExpose and has @Expose, pass
    boolean hasExpose = expose != null;
    boolean isFindAndFill = false;
    // 没有 @Expose 或 有但不跳过，则需要寻找token
    if (!hasExpose || !expose.skip()) {
      isFindAndFill = AuthUtil.findAndFill(token);
    }
    // 没有 @NotExpose 并 有@Expose 可以不拦截
    boolean hasNotExpose = AnnotationUtil.hasAnnotation(method, NotExpose.class);
    if (!hasNotExpose && hasExpose) {
      return true;
    }
    // has token but can not get user info, Unauthorized
    if (!isFindAndFill) {
      throw new UnauthorizedSecretException();
    }
    return true;
  }
}
