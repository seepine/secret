package com.seepine.secret.test;

import com.seepine.secret.AuthUtil;
import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.impl.DefaultBanServiceImpl;
import com.seepine.secret.impl.DefaultPermissionServiceImpl;
import com.seepine.secret.impl.DefaultTokenServiceImpl;
import com.seepine.secret.properties.AuthProperties;
import java.util.Arrays;
import java.util.HashSet;

public class ExpiresTest {
  public static void main(String[] args) throws InterruptedException {
    AuthProperties properties = new AuthProperties();
    AuthUtil.init(
        new AuthProperties(),
        new DefaultTokenServiceImpl(properties),
        new DefaultPermissionServiceImpl(properties),
        new DefaultBanServiceImpl(properties));

    AuthUser user =
        AuthUser.builder()
            .id(123456L)
            .nickName("secret")
            .permissions(new HashSet<>(Arrays.asList("sys_add", "sys_del")))
            .build();

    AuthUser newUser = AuthUtil.login(user, 2);
    System.out.println(newUser);
    // 放开此行则直接过期，依赖Cache.set
    // AuthUtil.logout();
    Thread.sleep(1000);
    System.out.println("1s之后");
    AuthUtil.findAndFill(newUser.getTokenInfo().getAccessToken());
    System.out.println(AuthUtil.getUser());
    Thread.sleep(1000);
    System.out.println("2s之后");
    AuthUtil.findAndFill(newUser.getTokenInfo().getAccessToken());
    System.out.println(AuthUtil.getUser());
  }
}
