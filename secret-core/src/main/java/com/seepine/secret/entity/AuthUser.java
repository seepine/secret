package com.seepine.secret.entity;

import java.io.Serializable;
import java.util.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * @author seepine
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
@SuperBuilder(toBuilder = true)
public class AuthUser implements Serializable {
  private static final long serialVersionUID = 0L;

  /** 主键id */
  private String id;

  /** 名称 */
  private String name;

  /** 用户的昵称 */
  private String nickname;

  /** 电子邮件地址 */
  private String email;

  /** 电子邮件地址是否经过验证 */
  private Boolean emailVerified;

  /** 手机号 */
  private String phoneNumber;

  /** 手机号是否已验证 */
  private Boolean phoneNumberVerified;

  /** 头像图片的URL。 */
  private String picture;

  /** 性别 */
  private String gender;

  /** 地址 */
  private String address;

  /** 租户id */
  private String tenantId;

  /** 平台 */
  private String platform;

  /** 登录时间,自动生成,时间戳,单位秒 */
  private Long signAt;

  /** 续期时间,自动生成,时间戳,单位秒 */
  private Long refreshAt;

  /** 额外参数 */
  @Builder.Default private Map<String, Object> claims = new HashMap<>();

  /** 用户角色 */
  private Set<String> roles;

  /** 用户权限 */
  private Set<String> permissions;

  public AuthUser withClaim(String key, String value) {
    claims.put(key, value);
    return this;
  }

  public AuthUser withClaim(String key, Boolean value) {
    claims.put(key, value);
    return this;
  }

  public AuthUser withClaim(String key, Integer value) {
    claims.put(key, value);
    return this;
  }

  public AuthUser withClaim(String key, Long value) {
    claims.put(key, value);
    return this;
  }

  public AuthUser withClaim(String key, Double value) {
    claims.put(key, value);
    return this;
  }

  public AuthUser withClaim(String key, Date value) {
    claims.put(key, value);
    return this;
  }

  public AuthUser withClaim(String key, List<?> value) {
    claims.put(key, value);
    return this;
  }

  public Object getClaimAsObj(String claimKey) {
    if (claims == null) {
      return null;
    }
    return claims.get(claimKey);
  }

  public String getClaim(String claimKey) {
    Object val = getClaimAsObj(claimKey);
    if (val == null) {
      return null;
    }
    if (val instanceof String) {
      return (String) val;
    }
    return val.toString();
  }

  public Integer getClaimAsInt(String claimKey) {
    return Optional.ofNullable(getClaim(claimKey)).map(Integer::valueOf).orElse(null);
  }

  public Double getClaimAsDouble(String claimKey) {
    return Optional.ofNullable(getClaim(claimKey)).map(Double::valueOf).orElse(null);
  }

  public Boolean getClaimAsBool(String claimKey) {
    String val = getClaim(claimKey);
    if ("true".equals(val) || "1".equals(val)) {
      return true;
    }
    if ("false".equals(val) || "0".equals(val)) {
      return false;
    }
    throw new ClassCastException(val + " cannot be cast to class java.lang.Boolean");
  }

  public Long getClaimAsLong(String claimKey) {
    return Optional.ofNullable(getClaim(claimKey)).map(Long::valueOf).orElse(null);
  }

  public Date getClaimAsDate(String claimKey) {
    return (Date) getClaimAsObj(claimKey);
  }

  public Long getIdAsLong() {
    return id == null ? null : Long.valueOf(id);
  }

  public Long getTenantIdAsLong() {
    return tenantId == null ? null : Long.valueOf(tenantId);
  }

  public AuthUser copy() {
    return toBuilder().build();
  }
}
