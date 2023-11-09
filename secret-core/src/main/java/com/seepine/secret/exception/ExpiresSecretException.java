package com.seepine.secret.exception;

import com.seepine.tool.util.LocalDateTimeUtil;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Authorization expires Exception
 *
 * @author seepine
 * @since 1.0.0
 */
public class ExpiresSecretException extends SecretException {
  private static final String DEFAULT_MSG = "Authorization expires on ";
  private final LocalDateTime expire;

  public ExpiresSecretException(Long expires) {
    this(Instant.ofEpochSecond(expires));
  }

  public ExpiresSecretException(Instant expires) {
    this(LocalDateTime.ofInstant(expires, ZoneId.systemDefault()));
  }

  public ExpiresSecretException(LocalDateTime localDateTime) {
    super(DEFAULT_MSG + LocalDateTimeUtil.format(localDateTime));
    expire = localDateTime;
  }

  public LocalDateTime getExpire() {
    return expire;
  }
}
