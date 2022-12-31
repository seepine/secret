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
  RATE_LIMIT("请求过于频繁"),
  NOT_REQUEST("无效的请求"),
  MISSING_ID("缺少id"),
  GEN_TOKEN_FAIL("生成token失败"),
  VERIFY_TOKEN_FAIL("验证token失败"),
  TOKEN_EXPIRES("登录过期"),
  INCORRECT_ISSUER("错误的签发者");
  public final String message;

  AuthExceptionType(String message) {
    this.message = message;
  }
}
