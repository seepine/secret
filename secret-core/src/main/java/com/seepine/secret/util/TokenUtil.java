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
		boolean isFindAndFill = AuthUtil.findAndFill(token);
		// not @NotExpose and has @Expose, pass
		if (!AnnotationUtil.hasAnnotation(method, NotExpose.class)
			&& AnnotationUtil.hasAnnotation(method, Expose.class)) {
			return true;
		}
		// token is blank, Not Acceptable
		if (Objects.isBlank(token)) {
			throw new UnauthorizedSecretException();
		}
		// has token but can not get user info, Unauthorized
		if (!isFindAndFill) {
			throw new UnauthorizedSecretException();
		}
		return true;
	}
}
