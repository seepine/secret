package com.seepine.secret.entity;

import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * token info
 *
 * @author seepine
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
@SuperBuilder(toBuilder = true)
public class TokenInfo implements Serializable {

  /** 访问token */
  private String accessToken;

  /** 访问token过期时间,秒,eg:3600 */
  private Long expiresIn;

  /** 刷新token */
  private String refreshToken;

  /** 刷新token过期时间,秒,eg:3600 */
  private Long refreshExpires;
}
