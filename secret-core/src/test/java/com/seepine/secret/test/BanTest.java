package com.seepine.secret.test;

import com.seepine.secret.AuthUtil;
import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.exception.BanSecretException;
import com.seepine.secret.impl.DefaultBanServiceImpl;
import com.seepine.secret.impl.DefaultPermissionServiceImpl;
import com.seepine.secret.impl.DefaultTokenServiceImpl;
import com.seepine.secret.properties.AuthProperties;
import java.util.Arrays;
import java.util.HashSet;

public class BanTest {
  public static void main(String[] args) throws InterruptedException {
    AuthProperties properties = new AuthProperties();
    AuthUtil.init(
        new AuthProperties(),
        new DefaultTokenServiceImpl(properties),
        new DefaultPermissionServiceImpl(properties),
        new DefaultBanServiceImpl(properties));
    AuthUser user =
        AuthUser.builder()
            .id("123456")
            .name("secret")
            .roles(new HashSet<>(Arrays.asList("admin", "manager")))
            .permissions(new HashSet<>(Arrays.asList("sys_add", "sys_edit")))
            .build()
            .withClaim("status", LoginTest.Status.ACTIVE.name())
            .withClaim("isDelete", false);
    System.out.println("登录：");
    AuthUtil.login(user);
    System.out.println(AuthUtil.getUser());

    System.out.println("禁用: commit");
    AuthUtil.ban("commit");

    try {
      System.out.println("verify ban: " + AuthUtil.banVerify("commit"));
      AuthUtil.banVerifyOrElseThrow("commit");
    } catch (BanSecretException e) {
      e.printStackTrace();
    }
    Thread.sleep(1000);
    System.out.println("取消禁用: commit");
    AuthUtil.banCancel("commit");

    System.out.println("verify ban: " + AuthUtil.banVerify("commit"));
  }
}
