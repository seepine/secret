package com.seepine.secret.exception;

/**
 * TokenAlgorithm Exception
 *
 * @author seepine
 * @since 1.0.0
 */
public class TokenSecretException extends SecretException {
  private static final String DEFAULT_MSG = "Token Wrong";

  public TokenSecretException() {
    super(DEFAULT_MSG);
  }

  public TokenSecretException(Throwable cause) {
    super(cause);
  }
}
