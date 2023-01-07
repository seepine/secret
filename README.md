# secret

极简安全框架，支持SpringBoot、Quarkus

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
    // 通过login方法会自动填充token属性 {id,nickName,token...}
    return AuthUtil.login(user);
  }

  @Expose
  @GetMapping("/login/{code}")
  public MyAuthUser login(@PathVariable String code) {
    // class MyAuthUser extend AuthUser{ 扩展自己想要的字段 }
    MyAuthUser myUser = userService.getByCode(code);
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

场景一般为用户修改了昵称或头像

```java
public class Controller {
  @GetMapping("/update")
  public AuthUser info() {
    // 修改用户信息逻辑
    AuthUser user = userService.update();
    // 刷新用户信息
    return AuthUtil.refresh(user);
  }
}
```

### 4.自定义配置

常用自定义的配置

```yml
secret:
  # jwt token有效期，单位秒，默认3天
  expires-at: 259200
  # jwt颁发者
  issuer: secret
  # jwt HS256密钥
  secret: yourSecret
```

### 5.自定义token生成解析逻辑

以 `SpringBoot` 为例，实现 `AuthService` 并注入bean即可

```java

@Configuration
@AutoConfigureBefore(SecretConfiguration.class)
public class Config {
  @Resource
  private AuthProperties authProperties;

  @Bean
  public AuthService authService() {
    return new CustomAuthService(authProperties);
  }
}
```

## 三、接口鉴权

> @Permission/@PrePermission

使用接口鉴权注解时，需要在登陆时传入用户所拥有的权限list，例如

```java
class Controller {

  @Expose
  @GetMapping("/login/{username}/{password}")
  public AuthUser login(@PathVariable String username, @PathVariable String password) {
    AuthUser user = userService.getByUsername(username, password);
    Set<String> permissions = roleService.getByUserId(user.getId());
    // user.setPermissions(permissions);
    // return AuthUtil.login(user);
    return AuthUtil.login(user, permissions);
  }

}
```

或

```java
```

### 1.单独使用Permission

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

  // 必须拥有 'edit' 和 'del' 权限才可访问
  @Permission({"edit", "del"})
  @GetMapping("/edit/and/del")
  public void editAndDel() {
  }

  // 拥有 'del_all' 或者 'administrator' 权限即可访问
  @Permission(or = {"del_all", "administrator"})
  @GetMapping("/del/all")
  public void delAll() {
  }


  @Resource
  Service service;

  @GetMapping("/del/all")
  public void func() {
    //也会需要鉴权，可得知@Permission不仅仅可加在接口上，只要是ioc接管的都可以（原理使用aop实现）
    service.func();
  }

}

@Service
class Service {
  @Permission("service_permission_a")
  public void func() {
  }
}
```

### 2.使用PermissionPrefix为所有权限加上前缀

正常使用场景中，一般的权限会如同`xxx_add`,`yyy_add`,`zzz_add`,`xxx_edit`
这般，前面带有模块或业务的标识，当然使用`@Permission`
直接指定具体权限也是可以的例如`@Permission("xxx_add")`，但是一般业务按Controller划分，同一个Controller中所有接口的权限前缀基本是相同的

```java

@PermissionPrefix("sys_user_")
@RestController
public class Controller {
  // 必须拥有'sys_user_add'权限才可访问
  @Permission("add")
  @GetMapping("/add")
  public void add() {
  }

  // 必须拥有'sys_user_edit'权限才可访问
  @Permission("edit")
  @GetMapping("/edit")
  public void edit() {
  }

  // 必须拥有'sys_role_edit'权限才可访问
  // prefix为false时，不会拼接类上@PermissionPrefix的前缀
  @Permission(value = "sys_role_edit", prefix = false)
  @GetMapping("/role/edit")
  public void roleEdit() {
  }
}
```

### 3.也可在业务逻辑中判断

```java
public class Service {
  public void save(User user) {
    AuthUtil.hasPermission("user_add");
  }
}
```

### 4.实现带鉴权功能的BaseController

一般业务都会有crud接口，所以我们可以抽离出BaseController结合PermissionPrefix快速实现crud接口并且拥有接口鉴权功能

- BaseController

```java
public class BaseController<S, T> {
  @Resource
  S service;

  @Permission("add")
  @GetMapping("/add")
  public void add(@RequestBody T entity) {
    service.add(entity);
  }

  @Permission("edit")
  @GetMapping("/edit")
  public void edit() {
    service.edit();
  }
}
```

- UserController

```java

@RestController
@PermissionPrefix("user_")
@RequestMapping("user")
public class UserController extends BaseController<UserService, User> {
}
```

此时实现了用户新增和编辑功能，并且新增和编辑需要拥有权限分别是`user_add`和`user_edit`，并且当重写父类方法时，权限注解仍然有效

## 四、日志记录（暂只支持SpringBoot）

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

### 2.日志存库

```java

@Component
public class MyAuthLogService implements AuthLogService {
  @Override
  public void save(LogEvent logEvent) {
    // 此处可以将日志自行保存到mysql等
  }
}
```
