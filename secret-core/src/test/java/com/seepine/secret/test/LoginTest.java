package com.seepine.secret.test;

import com.seepine.secret.AuthUtil;
import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.impl.DefaultPermissionServiceImpl;
import com.seepine.secret.impl.DefaultTokenServiceImpl;
import com.seepine.secret.properties.AuthProperties;

public class LoginTest {
  enum Status {
    ACTIVE,
    NOT_ACTIVE
  }

  public static void main(String[] args) throws InterruptedException {
    AuthProperties properties = new AuthProperties();
    AuthUtil.init(
        properties,
        new DefaultTokenServiceImpl(properties),
        new DefaultPermissionServiceImpl(properties));
    AuthUser user =
        AuthUser.builder()
            .id(123456L)
            .nickName("secret")
            //            .roles(new HashSet<>(Arrays.asList("admin", "manager")))
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
