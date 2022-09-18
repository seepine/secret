package com.seepine.secret.interfaces;

import com.seepine.secret.entity.LogEvent;

/**
 * 日志接口
 *
 * @author seepine
 */
public interface AuthLogService {
  /**
   * 保存日志逻辑
   *
   * @param logEvent 日志信息
   */
  void save(LogEvent logEvent);
}
