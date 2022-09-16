package com.seepine.secret.interfaces;

import com.seepine.secret.entity.LogEvent;

/**
 * 日志接口
 *
 * @author seepine
 */
public interface AuthLogService {
  void save(LogEvent logEvent);
}
