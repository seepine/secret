package com.seepine.secret.exception;
/**
 * Ban Exception
 *
 * @author seepine
 * @since 1.0.0
 */
public class BanSecretException extends SecretException {
  private static final String DEFAULT_MSG = "Ban";

  private String ban;

  public BanSecretException() {
    super(DEFAULT_MSG);
  }

  public BanSecretException(String message) {
    super(message);
  }

  public BanSecretException(String ban, String message) {
    super(message);
    this.ban = ban;
  }

  public BanSecretException(Throwable cause) {
    super(cause);
  }

  public String getBan() {
    return ban;
  }
}
