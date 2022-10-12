package com.seepine.secret.interfaces;

/**
 * @author seepine
 * @since 0.0.7
 */
public interface LockApplyAs<T> {
  /**
   * 执行带返回值方法
   *
   * @return T
   */
  T run();
}
