package com.seepine.secret.enums;
/**
 * 定义鉴权异常类型
 *
 * @author seepine
 */
public enum AuthExceptionType {
  /** 定义鉴权异常类型 */
  NOT_TOKEN("请先登录"),
  INVALID_TOKEN("请先登录"),
  NOT_PERMISSION("没有权限"),
  //  NOT_SECRET("请求错误"),
  //  INVALID_SECRET("请求无效"),
  //  EXPIRED_SECRET("请求无效"),
  RATE_LIMIT("请求过于频繁"),
  NOT_REQUEST("无效的请求"),
  MISSING_ID("缺少id");
  public final String message;

  AuthExceptionType(String message) {
    this.message = message;
  }
}
