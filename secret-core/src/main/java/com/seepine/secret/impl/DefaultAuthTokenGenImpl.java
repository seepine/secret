package com.seepine.secret.impl;

import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.enums.AuthExceptionType;
import com.seepine.secret.exception.AuthException;
import com.seepine.secret.interfaces.AuthTokenGen;
import com.seepine.secret.properties.AuthProperties;
import com.seepine.secret.util.CurrentTimeMillis;
import com.seepine.tool.Run;
import com.seepine.tool.secure.symmetric.AES;

/**
 * @author seepine
 * @since 0.0.6
 */
public class DefaultAuthTokenGenImpl implements AuthTokenGen {
  private final AES aes;

  public DefaultAuthTokenGenImpl(AuthProperties authProperties) {
    int secretLength = authProperties.getSecret().length();
    Run.isTrue(
        secretLength == 16 || secretLength == 24 || secretLength == 32,
        "secret properties:must be 16/24/32 bytes long");
    this.aes = new AES(authProperties.getSecret());
  }

  @Override
  public <T extends AuthUser> String gen(T authUser) throws AuthException {
    if (authUser.getId() == null || "".equals(authUser.getId().toString())) {
      throw new AuthException(AuthExceptionType.MISSING_ID);
    }
    return aes.encrypt(authUser.getId().toString() + CurrentTimeMillis.now()).toLowerCase();
  }
}