package com.seepine.secret.test;

import com.alibaba.ttl.threadpool.TtlExecutors;
import com.seepine.secret.AuthUtil;
import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.impl.DefaultBanServiceImpl;
import com.seepine.secret.impl.DefaultTokenServiceImpl;
import com.seepine.secret.properties.AuthProperties;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThreadTest {
  public static void main(String[] args) throws InterruptedException {
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

    new Thread(
            () -> {
              System.out.println("new thread\n " + AuthUtil.getUser());
              System.out.println();
            })
        .start();

    Executor threadPool = Executors.newFixedThreadPool(1);
    threadPool = TtlExecutors.getTtlExecutor(threadPool);

    threadPool.execute(
        () -> {
          System.out.println("threadPool task1\n " + AuthUtil.getUser());
          System.out.println();
        });
    AuthUtil.refresh(AuthUser.builder().id(2).build());
    Thread.sleep(500);
    threadPool.execute(
        () -> {
          System.out.println("threadPool task2\n " + AuthUtil.getUser());
          System.out.println();
          // 在子线程修改只会影响子线程后续，不会影响父线程数据
          AuthUtil.refresh(AuthUser.builder().id(3).build());
          System.out.println("threadPool task2 2\n " + AuthUtil.getUser());
          System.out.println();
        });

    Thread.sleep(500);
    threadPool.execute(
        () -> {
          System.out.println("threadPool task3\n " + AuthUtil.getUser());
          System.out.println();
        });

    Thread.sleep(500);
    System.out.println(AuthUtil.getUser());
  }
}
