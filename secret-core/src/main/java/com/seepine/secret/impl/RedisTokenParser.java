package com.seepine.secret.impl;

import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.enums.AuthExceptionType;
import com.seepine.secret.exception.AuthException;
import com.seepine.secret.interfaces.TokenParser;
import com.seepine.secret.properties.AuthProperties;
import com.seepine.secret.util.RedisUtil;
import com.seepine.tool.secure.symmetric.AES;

import java.util.concurrent.TimeUnit;

/**
 * @author seepine
 */
public class RedisTokenParser implements TokenParser {
  private final AES aes;
  private final AuthProperties authProperties;
  private static final String CACHE_PREFIX = "com.seepine.secret:";

  public RedisTokenParser(AuthProperties authProperties) {
    this.aes = new AES(authProperties.getSecret());
    this.authProperties = authProperties;
  }

  @Override
  public <T extends AuthUser> T get(String token) {
    try {
      return RedisUtil.get(CACHE_PREFIX + token);
    } catch (Exception e) {
      throw new AuthException(AuthExceptionType.INVALID_TOKEN);
    }
  }

  @Override
  public <T extends AuthUser> void set(T authUser) {
    RedisUtil.set(
        CACHE_PREFIX + authUser.getToken(),
        authUser,
        authProperties.getTimeout(),
        TimeUnit.SECONDS);
  }

  @Override
  public <T extends AuthUser> T gen(T authUser) throws AuthException {
    if (authUser.getId() == null || "".equals(authUser.getId().toString())) {
      throw new AuthException(AuthExceptionType.MISSING_ID);
    }
    String token = aes.encrypt(authUser.getId().toString() + System.currentTimeMillis());
    authUser.setToken(token);
    return authUser;
  }

  @Override
  public <T extends AuthUser> void remove(T authUser) {
    RedisUtil.remove(CACHE_PREFIX + authUser.getToken());
  }
}
