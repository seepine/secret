package com.seepine.secret.entity;

/**
 * @author seepine
 */
public class LogEvent {
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

  public String getModule() {
    return module;
  }

  public LogEvent setModule(String module) {
    this.module = module;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public LogEvent setTitle(String title) {
    this.title = title;
    return this;
  }

  public String getContent() {
    return content;
  }

  public LogEvent setContent(String content) {
    this.content = content;
    return this;
  }

  public String getRequestUri() {
    return requestUri;
  }

  public LogEvent setRequestUri(String requestUri) {
    this.requestUri = requestUri;
    return this;
  }

  public String getMethod() {
    return method;
  }

  public LogEvent setMethod(String method) {
    this.method = method;
    return this;
  }

  public String getUserAgent() {
    return userAgent;
  }

  public LogEvent setUserAgent(String userAgent) {
    this.userAgent = userAgent;
    return this;
  }

  public String getHeaders() {
    return headers;
  }

  public LogEvent setHeaders(String headers) {
    this.headers = headers;
    return this;
  }

  public String getParams() {
    return params;
  }

  public LogEvent setParams(String params) {
    this.params = params;
    return this;
  }

  public String getBody() {
    return body;
  }

  public LogEvent setBody(String body) {
    this.body = body;
    return this;
  }

  public String getContentType() {
    return contentType;
  }

  public LogEvent setContentType(String contentType) {
    this.contentType = contentType;
    return this;
  }

  public String getClientIp() {
    return clientIp;
  }

  public LogEvent setClientIp(String clientIp) {
    this.clientIp = clientIp;
    return this;
  }

  public Long getExecutionTime() {
    return executionTime;
  }

  public LogEvent setExecutionTime(Long executionTime) {
    this.executionTime = executionTime;
    return this;
  }

  public String getException() {
    return exception;
  }

  public LogEvent setException(String exception) {
    this.exception = exception;
    return this;
  }

  public String getExceptionStackTrace() {
    return exceptionStackTrace;
  }

  public LogEvent setExceptionStackTrace(String exceptionStackTrace) {
    this.exceptionStackTrace = exceptionStackTrace;
    return this;
  }

  public AuthUser getUser() {
    return user;
  }

  public LogEvent setUser(AuthUser user) {
    this.user = user;
    return this;
  }
}
