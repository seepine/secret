package com.seepine.secret.interfaces;

import java.time.Duration;

public interface Cache {
  Object get(String key);

  void set(String key, Object val);

  void remove(String key);

  void expire(String key, Duration duration);
}
