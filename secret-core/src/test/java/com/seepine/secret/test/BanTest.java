package com.seepine.secret.test;

import com.seepine.secret.AuthUtil;
import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.exception.BanSecretException;
import com.seepine.secret.impl.DefaultBanServiceImpl;
import com.seepine.secret.impl.DefaultTokenServiceImpl;
import com.seepine.secret.properties.AuthProperties;
import java.util.Arrays;
import java.util.HashSet;

public class BanTest {
  public static void main(String[] args) {
    AuthProperties properties = new AuthProperties();
    AuthUtil.init(
        new AuthProperties(),
        new DefaultTokenServiceImpl(properties),
        new DefaultBanServiceImpl(properties));
    AuthUser user =
        AuthUser.builder()
            .id(123456L)
            .nickName("secret")
            .roles(new HashSet<>(Arrays.asList("admin", "manager")))
            .permissions(new HashSet<>(Arrays.asList("sys_add", "sys_edit")))
            .tenantName("myTenantName")
            .withClaim("status", LoginTest.Status.ACTIVE.name())
            .withClaim("isDelete", false)
            .build();
    AuthUtil.login(user);

    System.out.println(AuthUtil.getUser());

    AuthUtil.ban("commit");

    try {
      AuthUtil.banVerifyOrElseThrow("commit");
    } catch (BanSecretException e) {
      System.out.println("be banned: " + e.getBan());
      e.printStackTrace();
    }
    AuthUtil.banCancel("commit");

    System.out.println("verify ban: " + AuthUtil.banVerify("commit"));
  }
}
