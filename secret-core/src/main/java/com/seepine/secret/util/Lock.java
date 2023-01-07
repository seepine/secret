package com.seepine.secret.util;

import com.seepine.secret.interfaces.LockApply;
import com.seepine.secret.interfaces.LockApplyAs;

/**
 * 当redisson存在时，使用分布式锁，否则使用synchronized
 *
 * @author seepine
 * @since 0.0.7
 */
public class Lock {
  /**
   * 锁定运行
   *
   * @param key 锁值
   * @param apply 执行方法
   */
  public static void sync(Object key, LockApply apply) {
    synchronized (key.toString().intern()) {
      apply.run();
    }
  }
  /**
   * 锁定运行，有返回值
   *
   * @param key 锁值
   * @param apply 执行方法
   * @param <T> 返回值类型
   * @return 返回值
   */
  public static <T> T sync(Object key, LockApplyAs<T> apply) {
    synchronized (key.toString().intern()) {
      return apply.run();
    }
  }
}
