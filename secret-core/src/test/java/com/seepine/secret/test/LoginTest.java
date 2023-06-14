package com.seepine.secret.test;

import com.seepine.secret.AuthUtil;
import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.impl.DefaultTokenServiceImpl;
import com.seepine.secret.properties.AuthProperties;

import java.util.Arrays;
import java.util.HashSet;

public class LoginTest {
  enum Status {
    ACTIVE,
    NOT_ACTIVE
  }

  public static void main(String[] args) throws InterruptedException {
    AuthProperties properties = new AuthProperties();
    AuthUtil.init(new AuthProperties(), new DefaultTokenServiceImpl(properties));
    AuthUser user =
      AuthUser.builder()
        .id(123456L)
        .nickName("secret")
        .roles(new HashSet<>(Arrays.asList("admin", "manager")))
        .permissions(new HashSet<>(Arrays.asList("sys_add", "sys_edit")))
        .tenantName("myTenantName")
        .withClaim("status", Status.ACTIVE.name())
        .withClaim("isDelete", false)
        .build();

    AuthUser newUser = AuthUtil.login(user);
    System.out.println(newUser);
    Thread.sleep(2000);

    AuthUtil.refresh();
    System.out.println(AuthUtil.getUser());
    System.out.println(AuthUtil.getUser().getClaim("status"));
    System.out.println(AuthUtil.getUser().getClaimAsBool("isDelete"));
    Long signAt = AuthUtil.getUser().getSignAt();
    System.out.println("signAt:" + signAt);
    Long id = AuthUtil.getUser().getIdAsLong();
    System.out.println("id:" + id);
  }
}
