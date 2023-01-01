package com.seepine.secret.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
  /** 用户权限 */
  Set<String> permissions;
  /** 租户名称 */
  String tenantName;
  /** 租户id */
  String tenantId;
  /** 令牌 */
  String token;

  @JsonIgnore
  public Long getIdAsLong() {
    return Long.valueOf(id);
  }

  @JsonIgnore
  public Long getTenantIdAsLong() {
    return Long.valueOf(tenantId);
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

    public AuthUser build() {
      return authUser;
    }
  }
}
