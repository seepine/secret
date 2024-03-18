package com.seepine.secret.oauth2.token.dto.base;

import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * 令牌端点基类
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
public class TokenBaseDto implements Serializable {
  private static final long serialVersionUID = 0L;

  /** 客户端id */
  String client_id;

  /** 客户端secret */
  String client_secret;

  /**
   * 授权模式
   *
   * <p>授权码模式：authorization_code->TokenAuthorizationCodeDto<br>
   * 密码模式：password->TokenPasswordDto<br>
   * 客户端模式：client_credentials->TokenClientCredentialsDto<br>
   * 刷新令牌：refresh_token->TokenRefreshTokenDto
   */
  String grant_type;
}
