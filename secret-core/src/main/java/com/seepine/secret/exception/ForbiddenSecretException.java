package com.seepine.secret.exception;

/**
 * Forbidden Exception
 *
 * @author seepine
 * @since 1.0.0
 */
public class ForbiddenSecretException extends SecretException {
	private static final String DEFAULT_MSG = "Forbidden";

	public ForbiddenSecretException() {
		super(DEFAULT_MSG);
	}

	public ForbiddenSecretException(String message) {
		super(message);
	}

	public ForbiddenSecretException(Throwable cause) {
		super(cause);
	}
}
