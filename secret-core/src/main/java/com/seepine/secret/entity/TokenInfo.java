package com.seepine.secret.entity;

import com.seepine.tool.Run;
import java.util.HashMap;
import java.util.Map;

/**
 * token info
 *
 * @author seepine
 * @since 1.0.0
 */
public class TokenInfo {
  /** 访问token */
  private String accessToken;

  /** 访问token过期时间,秒 */
  private Long expires;

  /** 刷新token */
  private String refreshToken;

  /** 刷新token过期时间,秒 */
  private Long refreshExpires;

  /** 登录时间,自动生成,秒 */
  private Long signAt;

  /** 续期时间,自动生成,秒 */
  private Long refreshAt;

  public String getAccessToken() {
    return accessToken;
  }

  public TokenInfo setAccessToken(String accessToken) {
    this.accessToken = accessToken;
    return this;
  }

  public Long getExpires() {
    return expires;
  }

  public TokenInfo setExpires(Long expires) {
    this.expires = expires;
    return this;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public TokenInfo setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
    return this;
  }

  public Long getRefreshExpires() {
    return refreshExpires;
  }

  public TokenInfo setRefreshExpires(Long refreshExpires) {
    this.refreshExpires = refreshExpires;
    return this;
  }

  public Long getSignAt() {
    return signAt;
  }

  public TokenInfo setSignAt(Long signAt) {
    this.signAt = signAt;
    return this;
  }

  public Long getRefreshAt() {
    return refreshAt;
  }

  public TokenInfo setRefreshAt(Long refreshAt) {
    this.refreshAt = refreshAt;
    return this;
  }

  public static TokenInfo parse(Map<String, Object> map) {
    TokenInfo tokenInfo = new TokenInfo();
    Run.nonNull(map.get("accessToken"), val -> tokenInfo.setAccessToken(val.toString()));
    Run.nonNull(map.get("refreshToken"), val -> tokenInfo.setRefreshToken(val.toString()));
    Run.nonNull(map.get("expires"), val -> tokenInfo.setExpires(Long.valueOf(val.toString())));
    Run.nonNull(
        map.get("refreshExpires"),
        val -> tokenInfo.setRefreshExpires(Long.valueOf(val.toString())));
    Run.nonNull(map.get("signAt"), val -> tokenInfo.setSignAt(Long.valueOf(val.toString())));
    Run.nonNull(map.get("refreshAt"), val -> tokenInfo.setRefreshAt(Long.valueOf(val.toString())));
    return tokenInfo;
  }

  public Map<String, String> toMap() {
    Map<String, String> map = new HashMap<>();
    if (accessToken != null) {
      map.put("accessToken", accessToken);
    }
    if (refreshToken != null) {
      map.put("refreshToken", refreshToken);
    }
    if (expires != null) {
      map.put("expires", expires.toString());
    }
    if (refreshExpires != null) {
      map.put("refreshExpires", refreshExpires.toString());
    }
    if (signAt != null) {
      map.put("signAt", signAt.toString());
    }
    if (refreshAt != null) {
      map.put("refreshAt", refreshAt.toString());
    }
    return map;
  }

  public TokenInfo copy() {
    return new TokenInfo()
        .setAccessToken(accessToken)
        .setRefreshToken(refreshToken)
        .setExpires(expires)
        .setRefreshExpires(refreshExpires)
        .setSignAt(signAt)
        .setRefreshAt(refreshAt);
  }

  @Override
  public String toString() {
    return "TokenInfo{"
        + "accessToken='"
        + accessToken
        + '\''
        + ", expires="
        + expires
        + ", refreshToken='"
        + refreshToken
        + '\''
        + ", refreshExpires="
        + refreshExpires
        + ", signAt="
        + signAt
        + ", refreshAt="
        + refreshAt
        + '}';
  }
}
