package com.seepine.secret.entity;

import java.io.Serializable;
import java.util.*;

/**
 * @author seepine
 */
public class AuthUser implements Serializable {
  private static final long serialVersionUID = 0L;
  /** 主键id */
  private String id;
  /** 昵称 */
  private String nickName;
  /** 姓名 */
  private String fullName;
  /** 用户名 */
  private String username;
  /** 手机号 */
  private String phone;
  /** 电子邮箱 */
  private String email;
  /** 头像url */
  private String avatarUrl;
  /** 登录时间,自动生成,second */
  private Long signAt;
  /** 续期时间,自动生成,second */
  private Long refreshAt;
  /** 过期时间,null表示无过期，second */
  private Long expiresAt;
  /** 租户名称 */
  private String tenantName;
  /** 租户id */
  private String tenantId;
  /** 平台 */
  private String platform;
  /** 额外参数 */
  private Map<String, Object> claims = new HashMap<>();
  /** 用户角色 */
  private Set<String> roles;
  /** 用户权限 */
  private Set<String> permissions;
  /** 令牌 */
  private String token;

  public String getId() {
    return id;
  }

  public AuthUser setId(String id) {
    this.id = id;
    return this;
  }

  public String getNickName() {
    return nickName;
  }

  public AuthUser setNickName(String nickName) {
    this.nickName = nickName;
    return this;
  }

  public String getFullName() {
    return fullName;
  }

  public AuthUser setFullName(String fullName) {
    this.fullName = fullName;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public AuthUser setUsername(String username) {
    this.username = username;
    return this;
  }

  public String getPhone() {
    return phone;
  }

  public AuthUser setPhone(String phone) {
    this.phone = phone;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public AuthUser setEmail(String email) {
    this.email = email;
    return this;
  }

  public String getAvatarUrl() {
    return avatarUrl;
  }

  public AuthUser setAvatarUrl(String avatarUrl) {
    this.avatarUrl = avatarUrl;
    return this;
  }

  public Long getSignAt() {
    return signAt;
  }

  public AuthUser setSignAt(Long signAt) {
    this.signAt = signAt;
    return this;
  }

  public Long getRefreshAt() {
    return refreshAt;
  }

  public AuthUser setRefreshAt(Long refreshAt) {
    this.refreshAt = refreshAt;
    return this;
  }

  public Long getExpiresAt() {
    return expiresAt;
  }

  public AuthUser setExpiresAt(Long expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  public String getTenantName() {
    return tenantName;
  }

  public AuthUser setTenantName(String tenantName) {
    this.tenantName = tenantName;
    return this;
  }

  public String getTenantId() {
    return tenantId;
  }

  public AuthUser setTenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

  public String getPlatform() {
    return platform;
  }

  public AuthUser setPlatform(String platform) {
    this.platform = platform;
    return this;
  }

  public Set<String> getRoles() {
    return roles;
  }

  public AuthUser setRoles(Set<String> roles) {
    this.roles = roles;
    return this;
  }

  public Set<String> getPermissions() {
    return permissions;
  }

  public AuthUser setPermissions(Set<String> permissions) {
    this.permissions = permissions;
    return this;
  }

  public Map<String, Object> getClaims() {
    return claims;
  }

  public AuthUser setClaims(Map<String, Object> claims) {
    this.claims = claims;
    return this;
  }

  public String getToken() {
    return token;
  }

  public AuthUser setToken(String token) {
    this.token = token;
    return this;
  }

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

  @Override
  public String toString() {
    return "AuthUser{"
        + "id='"
        + id
        + '\''
        + ", nickName='"
        + nickName
        + '\''
        + ", fullName='"
        + fullName
        + '\''
        + ", username='"
        + username
        + '\''
        + ", phone='"
        + phone
        + '\''
        + ", email='"
        + email
        + '\''
        + ", avatarUrl='"
        + avatarUrl
        + '\''
        + ", signAt="
        + signAt
        + ", refreshAt="
        + refreshAt
        + ", expiresAt="
        + expiresAt
        + ", tenantName='"
        + tenantName
        + '\''
        + ", tenantId='"
        + tenantId
        + '\''
        + ", platform='"
        + platform
        + '\''
        + ", claims="
        + claims
        + ", roles="
        + roles
        + ", permissions="
        + permissions
        + ", token='"
        + token
        + '\''
        + '}';
  }

  public AuthUser copy() {
    Set<String> cloneRoles = new HashSet<>(16);
    if (!Objects.isNull(roles)) {
      cloneRoles.addAll(roles);
    }
    Set<String> clonePermissions = new HashSet<>(16);
    if (!Objects.isNull(permissions)) {
      clonePermissions.addAll(permissions);
    }
    Map<String, Object> cloneClaims = new HashMap<>(16);
    if (!Objects.isNull(claims)) {
      cloneClaims.putAll(claims);
    }
    return AuthUser.builder()
        .id(id)
        .nickName(nickName)
        .fullName(fullName)
        .username(username)
        .phone(phone)
        .email(email)
        .avatarUrl(avatarUrl)
        .signAt(signAt)
        .refreshAt(refreshAt)
        .expiresAt(expiresAt)
        .tenantId(tenantId)
        .tenantName(tenantName)
        .platform(platform)
        .roles(cloneRoles)
        .permissions(clonePermissions)
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

    public Builder username(String username) {
      authUser.setUsername(username);
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

    public Builder signAt(Long signAt) {
      authUser.setSignAt(signAt);
      return this;
    }

    public Builder refreshAt(Long signAt) {
      authUser.setRefreshAt(signAt);
      return this;
    }

    public Builder expiresAt(Long expiresAt) {
      authUser.setExpiresAt(expiresAt);
      return this;
    }

    public Builder roles(Set<String> roles) {
      authUser.roles = roles;
      return this;
    }

    public Builder permissions(Set<String> permissions) {
      authUser.permissions = permissions;
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

    public Builder platform(String platform) {
      authUser.setPlatform(platform);
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

    public Builder withClaim(String key, List<?> value) {
      authUser.claims.put(key, value);
      return this;
    }

    public AuthUser build() {
      return authUser;
    }
  }
}
