package com.seepine.secret.exception;

/**
 * Unauthorized Exception
 *
 * @author seepine
 * @since 1.0.0
 */
public class UnauthorizedSecretException extends SecretException {
	private static final String DEFAULT_MSG = "Unauthorized";

	public UnauthorizedSecretException() {
		super(DEFAULT_MSG);
	}

	public UnauthorizedSecretException(String message) {
		super(message);
	}

	public UnauthorizedSecretException(Throwable cause) {
		super(cause);
	}
}
