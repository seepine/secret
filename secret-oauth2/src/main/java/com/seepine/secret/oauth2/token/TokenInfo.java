package com.seepine.secret.oauth2.token;

import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * 令牌端点返回参数
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
public class TokenInfo implements Serializable {
  private static final long serialVersionUID = 0L;

  /** 访问令牌，必填 */
  String access_token;

  /** 令牌类型，通常为 "Bearer"，必填 */
  String token_type;

  /** 访问令牌的过期时间（以秒为单位），必填 */
  Long expires_in;

  /** 授权范围，例如 profile email，多个用空格隔开 */
  String scope;

  /** 刷新令牌，scope包含offline_access时应返回 */
  String refresh_token;

  /** 该值应该是个jwt格式字符串，scope包含 openid时应返回 */
  String id_token;
}
