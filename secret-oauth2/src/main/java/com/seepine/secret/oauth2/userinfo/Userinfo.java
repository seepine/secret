package com.seepine.secret.oauth2.userinfo;

import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * 用户信息点端
 *
 * @author seepine
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
public class Userinfo implements Serializable {
  private static final long serialVersionUID = 0L;

  /** 用户唯一标识，例如用户openid，不建议直接返回用户id，否则三方客户端有可能数据共享 */
  String sub;

  // scope=profile应返回
  /** 名称，姓名全称或用户名等 */
  String name;

  /** 昵称 */
  String nickname;

  /** 头像url地址 */
  String picture;

  /** 性别 */
  String gender;

  /** 用户的出生日期 */
  String birthdate;

  // scope=address应返回
  /** 地址 */
  String address;

  // scope=email应返回
  /** 邮箱 */
  String email;

  /** 邮箱是否验证 */
  Boolean email_verified;

  // scope=phone应返回
  /** 手机号 */
  String phone_number;

  /** 手机号是否验证 */
  Boolean phone_number_verified;
}
