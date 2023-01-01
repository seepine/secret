package com.seepine.secret.test;

import com.seepine.secret.AuthUtil;
import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.impl.DefaultAuthServiceImpl;
import com.seepine.secret.properties.AuthProperties;

public class LoginTest {
  public static void main(String[] args) throws InterruptedException {
    AuthProperties properties = new AuthProperties();
    AuthUtil.init(properties, new DefaultAuthServiceImpl(properties));
    AuthUser user = AuthUser.builder().id(123456L).nickName("secret").build();
    AuthUser newUser = AuthUtil.login(user);
    user.setId(null);
    System.out.println(user);
    System.out.println(newUser);
    Thread.sleep(2852);
    AuthUtil.refresh();
    AuthUtil.findAndFill(newUser.getToken());
    System.out.println(AuthUtil.getUser());
  }
}
