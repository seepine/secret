package com.seepine.secret.test;

import com.seepine.secret.AuthUtil;
import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.impl.DefaultCacheServiceImpl;
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
        properties, new DefaultTokenServiceImpl(properties), new DefaultCacheServiceImpl());
    AuthUser user =
        AuthUser.builder()
            .id(123456L)
            .nickName("secret")
            .withClaim("tenantName", "myTenantName")
            .withClaim("status", Status.ACTIVE.name())
            .withClaim("isDelete", false)
            .build();
    AuthUser newUser = AuthUtil.login(user);
    user.setNickName(null);
    System.out.println(user);
    System.out.println(newUser);
    Thread.sleep(2852);
    AuthUtil.refresh();
    AuthUtil.findAndFill(newUser.getToken());
    System.out.println(AuthUtil.getUser());
    System.out.println(AuthUtil.getUser().getClaim("status"));
    System.out.println(AuthUtil.getUser().getClaimAsBool("isDelete"));
    Long signTime = AuthUtil.getUser().getSignTime();
    System.out.println("signTime:" + signTime);
    Long id = AuthUtil.getUser().getIdAsLong();
    System.out.println("id:" + id);
  }
}
