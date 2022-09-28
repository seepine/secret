package com.seepine.secret.entity;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author seepine
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser implements Serializable {
  private static final long serialVersionUID = 0L;
  /** 主键id */
  Serializable id;
  /** 昵称 */
  String nickName;
  /** 姓名 */
  String fullName;
  /** 手机号 */
  String phone;
  /** 电子邮箱 */
  String email;
  /** 头像url */
  String avatarUrl;
  /** 登录时间 */
  LocalDateTime signTime;
  /** 续期时间 */
  LocalDateTime refreshTime;
  /** 用户权限 */
  Set<String> permissions;
  /** 租户名称 */
  String tenantName;
  /** 租户id */
  Serializable tenantId;
  /** 令牌 */
  String token;
}
