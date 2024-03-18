package com.seepine.secret.entity;

import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * @author seepine
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
@SuperBuilder(toBuilder = true)
public class LogEvent implements Serializable {
  private static final long serialVersionUID = 0L;

  /** 模块，一般用于微服务或指定类型，方便统计 */
  private String module;

  /** 标题 */
  private String title;

  /** 内容 */
  private String content;

  /** 接口地址 */
  private String requestUri;

  /** 请求方式 */
  private String method;

  /** ua */
  private String userAgent;

  /** 请求头 */
  private String headers;

  /** 请求参数 */
  private String params;

  /** 请求体 */
  private String body;

  /** contentType */
  private String contentType;

  /** 客户端ip */
  private String clientIp;

  /** 执行时间 */
  private Long executionTime;

  /** 异常信息，null则表示正常执行 */
  private String exception;

  /** 异常时堆栈信息 */
  private String exceptionStackTrace;

  /** 用户信息，可能为空 */
  private AuthUser user;
}
