package com.seepine.secret.test;

import com.seepine.secret.AuthUtil;
import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.impl.DefaultAuthCacheImpl;
import com.seepine.secret.impl.DefaultAuthTokenGenImpl;
import com.seepine.secret.properties.AuthProperties;

public class LoginTest {
  public static void main(String[] args) {
    AuthProperties properties = new AuthProperties();
    AuthUtil.init(
        properties, new DefaultAuthCacheImpl(properties), new DefaultAuthTokenGenImpl(properties));
    AuthUser user = AuthUser.builder().id(123456L).nickName("seepine").build();
    AuthUser newUser = AuthUtil.login(user);
    user.setId(null);
    System.out.println(user);
    System.out.println(newUser);
  }
}
