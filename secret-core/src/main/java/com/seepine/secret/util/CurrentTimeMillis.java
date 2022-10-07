package com.seepine.secret.util;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author seepine
 */
public class CurrentTimeMillis {
  private volatile long now;

  private CurrentTimeMillis() {
    this.now = System.currentTimeMillis();
    scheduleTick();
  }

  private void scheduleTick() {
    new ScheduledThreadPoolExecutor(
            1,
            runnable -> {
              Thread thread = new Thread(runnable, "secret-current-time-millis");
              thread.setDaemon(true);
              return thread;
            })
        .scheduleAtFixedRate(() -> now = System.currentTimeMillis(), 1, 1, TimeUnit.MILLISECONDS);
  }

  public static long now() {
    return getInstance().now;
  }

  private static CurrentTimeMillis getInstance() {
    return SingletonHolder.INSTANCE;
  }

  private static class SingletonHolder {
    private static final CurrentTimeMillis INSTANCE = new CurrentTimeMillis();
  }
}
