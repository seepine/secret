package com.seepine.secret.spring.util;

import com.seepine.secret.AuthUtil;
import com.seepine.secret.annotation.Log;
import com.seepine.secret.entity.LogEvent;
import com.seepine.secret.exception.AuthException;
import com.seepine.tool.util.StrUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;

/**
 * @author seepine
 */
public class LogUtil {
  public static final String COLON = ":";
  public static final String LF = "\n";
  public static final String EQUAL = "=";
  public static final String AND = "&";
  public static final String SPACE = " ";

  public static LogEvent gen(Log log, Long executionTime, Throwable exception) {
    return gen(
        log.module(),
        StrUtil.isBlank(log.title()) ? log.value() : log.title(),
        log.content(),
        executionTime,
        exception);
  }

  public static LogEvent gen(String module, String title, String content) {
    return gen(module, title, content, null, null);
  }

  public static LogEvent gen(String module, String title, String content, Throwable exception) {
    return gen(module, title, content, null, exception);
  }

  public static LogEvent gen(
      String module, String title, String content, Long executionTime, Throwable exception) {
    HttpServletRequest request = HttpServletUtil.getHttpRequest();
    LogEvent logEvent = new LogEvent();
    logEvent.setExecutionTime(executionTime);
    logEvent.setModule(module);
    logEvent.setTitle(title);
    logEvent.setContent(content);
    if (request != null) {
      logEvent.setClientIp(IpUtil.getIp(request));
      logEvent.setRequestUri(request.getRequestURI());
      logEvent.setMethod(request.getMethod());
      logEvent.setUserAgent(request.getHeader("User-Agent"));
      logEvent.setParams(printMap(request.getParameterMap()));
      logEvent.setContentType(request.getContentType());
      logEvent.setHeaders(printHeader(request, request.getHeaderNames()));
    }
    if (exception != null) {
      logEvent.setException(exception.toString());
      logEvent.setExceptionStackTrace(printStackTrace(exception));
    }
    try {
      // ???????????????????????????
      logEvent.setUser(AuthUtil.getUser());
    } catch (AuthException ignored) {
    }
    return logEvent;
  }

  private static String printMap(Map<String, String[]> params) {
    StringBuilder str = new StringBuilder();
    for (Map.Entry<String, String[]> stringEntry : params.entrySet()) {
      String key = stringEntry.getKey();
      String[] value = stringEntry.getValue();
      str.append(key).append(EQUAL);
      if (value != null) {
        if (value.length == 1) {
          str.append(value[0]);
        } else {
          str.append(Arrays.toString(value));
        }
      }
      str.append(key).append(AND);
    }
    return str.length() > 0 ? str.substring(0, str.length() - 1) : str.toString();
  }

  private static String printStackTrace(Throwable e) {
    StringBuilder str = new StringBuilder();
    if (e.getStackTrace() != null) {
      for (StackTraceElement stackTraceElement : e.getStackTrace()) {
        str.append(stackTraceElement.toString()).append(LF);
      }
    }
    return str.length() > 0 ? str.substring(0, str.length() - 1) : str.toString();
  }

  private static String printHeader(HttpServletRequest request, Enumeration<String> headers) {
    StringBuilder str = new StringBuilder();
    while (headers.hasMoreElements()) {
      String header = headers.nextElement();
      if (StrUtil.isNotBlank(header)) {
        String headerValue = request.getHeader(header);
        if (StrUtil.isNotBlank(headerValue)) {
          str.append(header).append(COLON).append(SPACE).append(headerValue).append(LF);
        }
      }
    }
    return str.length() > 0 ? str.substring(0, str.length() - 1) : str.toString();
  }
}
