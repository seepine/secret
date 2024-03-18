package com.seepine.secret.oauth2.authorize;

import java.io.Serializable;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 授权端点
 *
 * @author seepine
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class AuthorizeDto implements Serializable {
  /** 客户端id */
  String client_id;

  /** 重定向的URI，可选 */
  String redirect_uri;

  /**
   * 表示授权范围，可选，多个用空格隔开 <br>
   * eg. profile email phone openid
   */
  String scope;

  /**
   * 表示授权类型，必选项。 <br>
   * 1.code 授权码模式，redirect_uri返回code <br>
   * 2.token 简化模式，redirect_uri直接返回access_token <br>
   * 3.id_token OIDC简化模式，redirect_uri返回id_token
   */
  String response_type;

  /** 由第三方客户端生成，重定向redirect_uri时原值返回 */
  String state;

  /** 由第三方客户端生成，防重放，在id_token中原值返回 */
  String nonce;
}
