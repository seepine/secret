package com.seepine.secret.exception;

/**
 * Secret Base Exception
 *
 * @author seepine
 * @since 1.0.0
 */
public class SecretException extends RuntimeException {
  private static final String DEFAULT_MSG = "Some Exception Happened";

  public SecretException() {
    super(DEFAULT_MSG);
  }

  public SecretException(String message) {
    super(message);
  }

  public SecretException(Throwable cause) {
    super(cause);
  }

  public SecretException(String message, Throwable cause) {
    super(message, cause);
  }
}
