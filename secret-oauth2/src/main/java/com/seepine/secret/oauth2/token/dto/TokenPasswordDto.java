package com.seepine.secret.oauth2.token.dto;

import com.seepine.secret.oauth2.token.dto.base.TokenBaseDto;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * 密码模式
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
public class TokenPasswordDto extends TokenBaseDto {
  /** 用户名 */
  String username;

  /** 密码 */
  String password;
}
