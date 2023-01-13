package com.seepine.secret.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seepine.tool.Run;
import lombok.*;

import java.io.Serializable;
import java.util.*;

/**
 * @author seepine
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser implements Serializable {
  private static final long serialVersionUID = 0L;
  /** 主键id */
  String id;
  /** 昵称 */
  String nickName;
  /** 姓名 */
  String fullName;
  /** 手机号 */
  String phone;
  /** 电子邮箱 */
  String email;
  /** 头像url */
  String avatarUrl;
  /** 登录时间,自动生成,second */
  Long signTime;
  /** 续期时间,自动生成,second */
  Long refreshTime;
  /** 租户名称 */
  String tenantName;
  /** 租户id */
  String tenantId;
  /** 用户权限 */
  Set<String> permissions;
  /** 额外参数 */
  Map<String, Object> claims = new HashMap<>();
  /** 令牌 */
  String token;

  @JsonIgnore
  public Object getClaimAsObj(String claimKey) {
    if (claims == null) {
      return null;
    }
    return claims.get(claimKey);
  }

  @JsonIgnore
  public String getClaim(String claimKey) {
    return (String) getClaimAsObj(claimKey);
  }

  @JsonIgnore
  public Integer getClaimAsInt(String claimKey) {
    return (Integer) getClaimAsObj(claimKey);
  }

  @JsonIgnore
  public Double getClaimAsDouble(String claimKey) {
    return (Double) getClaimAsObj(claimKey);
  }

  @JsonIgnore
  public Boolean getClaimAsBool(String claimKey) {
    return (Boolean) getClaimAsObj(claimKey);
  }

  @JsonIgnore
  public Long getClaimAsLong(String claimKey) {
    return (Long) getClaimAsObj(claimKey);
  }

  @JsonIgnore
  public Date getClaimAsDate(String claimKey) {
    return (Date) getClaimAsObj(claimKey);
  }

  @JsonIgnore
  public Long getIdAsLong() {
    return id == null ? null : Long.valueOf(id);
  }

  @JsonIgnore
  public Long getTenantIdAsLong() {
    return tenantId == null ? null : Long.valueOf(tenantId);
  }

  public AuthUser copy() {
    Set<String> clonePermission = new HashSet<>(16);
    Run.notEmpty(permissions, clonePermission::addAll);
    Map<String, Object> cloneClaims = new HashMap<>(16);
    Run.notEmpty(claims, cloneClaims::putAll);
    return AuthUser.builder()
        .id(id)
        .nickName(nickName)
        .fullName(fullName)
        .phone(phone)
        .email(email)
        .avatarUrl(avatarUrl)
        .signTime(signTime)
        .refreshTime(refreshTime)
        .tenantId(tenantId)
        .tenantName(tenantName)
        .permissions(clonePermission)
        .claims(cloneClaims)
        .token(token)
        .build();
  }

  public static AuthUser.Builder builder() {
    return new AuthUser.Builder();
  }

  public static class Builder {
    AuthUser authUser;

    public Builder() {
      authUser = new AuthUser();
    }

    public Builder id(String id) {
      authUser.setId(id);
      return this;
    }

    public Builder id(Long id) {
      authUser.setId(id.toString());
      return this;
    }

    public Builder id(Integer id) {
      authUser.setId(id.toString());
      return this;
    }

    public Builder nickName(String nickName) {
      authUser.setNickName(nickName);
      return this;
    }

    public Builder fullName(String fullName) {
      authUser.setFullName(fullName);
      return this;
    }

    public Builder phone(String phone) {
      authUser.setPhone(phone);
      return this;
    }

    public Builder email(String email) {
      authUser.setEmail(email);
      return this;
    }

    public Builder avatarUrl(String avatarUrl) {
      authUser.setAvatarUrl(avatarUrl);
      return this;
    }

    public Builder signTime(Long signTime) {
      authUser.setSignTime(signTime);
      return this;
    }

    public Builder refreshTime(Long refreshTime) {
      authUser.setRefreshTime(refreshTime);
      return this;
    }

    public Builder permissions(Set<String> permissions) {
      authUser.setPermissions(permissions);
      return this;
    }

    public Builder permissions(List<String> permissions) {
      authUser.setPermissions(new HashSet<>(permissions));
      return this;
    }

    public Builder tenantId(String tenantId) {
      authUser.setTenantId(tenantId);
      return this;
    }

    public Builder tenantId(Long tenantId) {
      authUser.setTenantId(tenantId.toString());
      return this;
    }

    public Builder tenantName(String tenantName) {
      authUser.setTenantName(tenantName);
      return this;
    }

    public Builder token(String token) {
      authUser.setToken(token);
      return this;
    }

    public Builder claims(Map<String, Object> claims) {
      authUser.claims = claims;
      return this;
    }

    public Builder withClaim(String key, String value) {
      authUser.claims.put(key, value);
      return this;
    }

    public Builder withClaim(String key, Boolean value) {
      authUser.claims.put(key, value);
      return this;
    }

    public Builder withClaim(String key, Integer value) {
      authUser.claims.put(key, value);
      return this;
    }

    public Builder withClaim(String key, Long value) {
      authUser.claims.put(key, value);
      return this;
    }

    public Builder withClaim(String key, Double value) {
      authUser.claims.put(key, value);
      return this;
    }

    public Builder withClaim(String key, Date value) {
      authUser.claims.put(key, value);
      return this;
    }

    public AuthUser build() {
      return authUser;
    }
  }
}
