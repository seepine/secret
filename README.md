# secret

极简安全框架，支持 SpringBoot2.x、SpringBoot3.x、Quarkus3.x

## 一、集成

### 1.简介

- @Expose/@NotExpose 暴露/不暴露接口
- @Permission/@PermissionPrefix 接口鉴权，快速实现用户、角色、权限功能
- @Log 快速实现日志记录

### 1.引入依赖

- [secret-spring-boot-starter](./secret-spring-boot-starter)
- [secret-quarkus](./secret-quarkus)

## 二、基本使用

> @Expose/@NotExpose

### 1.获取token

登录接口使用注解`@Expose`暴露，获取到用户信息后调用AuthUtil.login

```java
public class Controller {
  @Expose
  @GetMapping("/login/{username}/{password}")
  public AuthUser login(@PathVariable String username, @PathVariable String password) {
    AuthUser user = userService.getByUsername(username, password);
    return AuthUtil.login(user);
  }

  @Expose
  @GetMapping("/login/{code}")
  public AuthUser login(@PathVariable String code) {
    AuthUser myUser = userService.getByCode(code);
    return AuthUtil.login(myUser);
  }
}
```

### 2.请求接口

请求接口时，请求头中加上`{'Authorization':'Bearer ${authUser.token}'}`
，即可在后端中通过AuthUtil.getUser()获取到当前登录者的用户信息

```java
public class Controller {
  @GetMapping("/info")
  public AuthUser info() {
    return AuthUtil.getUser();
  }
}
```

### 3.刷新用户信息

场景一般为用户修改了昵称或头像，或者让token续期

```java
public class Controller {
  @GetMapping("/update")
  public AuthUser info() {
    // 修改用户信息逻辑
    AuthUser user = userService.update();
    // 刷新用户信息
    // 默认token为jwt，刷新后token会改变，前端也需要跟着变动token值
    // 也可以注入内置的token实现类NormalTokenServiceImpl，非jwt实现，刷新token不会改变，无需前端配合
    return AuthUtil.refresh(user);
  }
}
```

### 4.自定义配置

常用自定义的配置

```yml
secret:
  # token有效期，单位秒，默认12小时
  expires: 43200
  # aes密钥
  secret: yourSecret
```

### 5.自定义token生成解析逻辑

以 `SpringBoot` 为例，实现 `TokenService` 并注入bean即可

```java

@Configuration
@AutoConfigureBefore(SecretConfiguration.class)
public class Config {
  @Resource
  private AuthProperties authProperties;

  @Bean
  public TokenService tokenService() {
    // 默认DefaultTokenServiceImpl为jwt实现，也可该为非jwt的NormalTokenServiceImpl
    // 当然也可以自定义token生成和解析规则类如CustomAuthService
    return new CustomAuthService(authProperties);
  }
}
```

## 三、接口权限限制

> @Permission

使用接口鉴权注解时，需要在登陆时传入用户所拥有的权限sets，例如

```java
class Controller {

  @Expose
  @GetMapping("/login")
  public AuthUser login(String username, String password) {
    AuthUser user = userService.getByUsername(username, password);
    // return AuthUtil.login(user);
    Set<String> permissions = roleService.getByUserId(user.getId());
    return AuthUtil.login(user, permissions);
  }

}
```

### 1.使用Permission

> @Permission必须加在接口上

```java

@RestController
public class Controller {

  // 必须拥有 'add' 权限才可访问
  @Permission("add")
  @GetMapping("/add")
  public void add() {
  }

  // 必须拥有 'edit' 权限才可访问
  @Permission("edit")
  @GetMapping("/edit")
  public void edit() {
  }

  // 同时拥有 'del' 和 'del_all' 权限才能访问
  @Permission({"del", "del_all"})
  @GetMapping("/del/all")
  public void delAll() {
  }

  // 拥有 'edit' 或 'del' 其中一个权限即可访问
  @Permission(or = {"edit", "del"})
  @GetMapping("/edit/and/del")
  public void editAndDel() {
  }
}
```

### 2.也可在业务逻辑中判断

```java
public class Service {
  public void save(User user) {
    AuthUtil.hasPermission("user_add");
  }
}
```

### 3.自定义权限缓存

默认缓存使用`com.seepine.tool.cache.Cache`,因此可通过增强`Cache`将缓存保存至`Redis`等实现持久化

## 四、接口角色限制

> @Role

使用接口鉴权注解时，需要在登陆时传入用户所拥有的角色sets，例如

```java
class Controller {

  @Expose
  @GetMapping("/login")
  public AuthUser login(String username, String password) {
    AuthUser user = userService.getByUsername(username, password);
    // return AuthUtil.login(user);
    Set<String> roles = roleService.getByUserId(user.getId());
    uset.setRoles(roles);
    return AuthUtil.login(user, permissions);
  }

}
```

### 1.使用@Role

> @Role必须加在接口上

```java

@RestController
public class Controller {

  // 必须拥有 'admin' 角色才能访问
  @Role("admin")
  @GetMapping("/del/all")
  public void delAll() {
  }

}
```

## 五、日志记录

> 仅提供注解，可自行通过aop去获取和保存到库

> @Log

### 1.注解使用

```java

@RestController
public class UserController {
  @Log("新增用户")
  @GetMapping("/add")
  public void add() {
  }

  // 可详细描述日志行为
  @Log(title = "编辑用户", content = "管理员编辑用户")
  @GetMapping("/edit")
  public void edit() {
  }

  // 错误时也会触发日志记录
  @Log("删除用户")
  @GetMapping("/del")
  public void rate3() {
    throw new Exception("用户不存在，删除失败");
  }
}
```

## 六、跨线程问题

> 在子线程中，请仅使用 `AuthUtil.getUser()` 去获取用户信息，勿使用 `AuthUtil.refresh()` 等修改方法

### 1.跨线程

```java

@RestController
public class UserController {

  @GetMapping("/info")
  public void info() {
    // 可获取到用户信息
    AuthUtil.getUser();

    new Thread(
      () -> {
        // 可正常获取到用户信息
        AuthUtil.getUser();
      })
      .start();
  }
}
```

### 2.跨线程池

```java

@RestController
public class UserController {

  @GetMapping("/info")
  public void info() {
    // 可获取到用户信息
    AuthUtil.getUser();

    Executor threadPool = Executors.newFixedThreadPool(1);
    threadPool = TtlExecutors.getTtlExecutor(threadPool);
    threadPool.execute(() -> {
      // 可获取到用户信息
      AuthUtil.getUser();
    });
  }

  @GetMapping("/info")
  public void info() {
    // 可获取到用户信息
    AuthUtil.getUser();

    Executor threadPool = Executors.newFixedThreadPool(1);
    // 若不方便改造线程池，可在执行时传通过Ttl构造
    threadPool.execute(TtlRunnable.get(() -> {
      // 可获取到用户信息
      AuthUtil.getUser();
    }));
  }
}
```

## 七、临时切换

```java
class Test {
  public static void main(String[] args) {

    AuthUtil.login(AuthUser.builder()
      .id(123456L)
      .build());

    System.out.println(AuthUtil.getUser());

    AuthUtil.execute(
      AuthUser.builder().id(2).build(),
      () -> {
        System.out.println("inner :");
        System.out.println(AuthUtil.getUser());
      });

    System.out.println("end :");
    System.out.println(AuthUtil.getUser());
  }
}
```
