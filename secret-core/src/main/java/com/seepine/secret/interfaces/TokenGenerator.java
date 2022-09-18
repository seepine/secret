package com.seepine.secret.interfaces;

/**
 * 生成token接口
 *
 * @author seepine
 */
public interface TokenGenerator {
  /**
   * 生成token值
   *
   * @return token
   */
  String gen();
}
