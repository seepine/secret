package com.seepine.secret.test;

import com.seepine.secret.AuthUtil;
import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.impl.DefaultBanServiceImpl;
import com.seepine.secret.impl.DefaultPermissionServiceImpl;
import com.seepine.secret.impl.DefaultTokenServiceImpl;
import com.seepine.secret.properties.AuthProperties;

public class ExecuteTest {
  public static void main(String[] args) {
    AuthProperties properties = new AuthProperties();
    AuthUtil.init(
        new AuthProperties(),
        new DefaultTokenServiceImpl(properties),
        new DefaultPermissionServiceImpl(properties),
        new DefaultBanServiceImpl(properties));
    AuthUser user = AuthUser.builder().id("123456").build();
    AuthUtil.login(user);

    System.out.println(AuthUtil.getUser());

    String runRes =
        AuthUtil.execute(
            AuthUser.builder().id("2").build(),
            () -> {
              System.out.println("inner :");
              System.out.println(AuthUtil.getUser());
              return AuthUtil.getUser().getId();
            });
    System.out.println("run res:" + runRes);

    System.out.println("end :");
    System.out.println(AuthUtil.getUser());
  }
}
