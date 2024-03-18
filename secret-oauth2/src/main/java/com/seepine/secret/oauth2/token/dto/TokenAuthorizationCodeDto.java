package com.seepine.secret.oauth2.token.dto;

import com.seepine.secret.oauth2.token.dto.base.TokenBaseDto;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * 授权码模式
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
public class TokenAuthorizationCodeDto extends TokenBaseDto {
  /** 授权端点重定向传递的code值 */
  String code;
}
