package com.seepine.secret.oauth2.token.dto;

import com.seepine.secret.oauth2.token.dto.base.TokenBaseDto;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * 刷新令牌
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
public class TokenRefreshTokenDto extends TokenBaseDto {
  /** 旧刷新令牌 */
  String refresh_token;
}
