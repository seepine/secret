package com.seepine.secret.spring.aspect;

import com.seepine.secret.annotation.Log;
import com.seepine.secret.entity.LogEvent;
import com.seepine.secret.interfaces.AuthLogService;
import com.seepine.secret.spring.util.LogUtil;
import com.seepine.tool.util.CurrentTimeMillis;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 日志aop
 *
 * @author seepine
 */
@Aspect
@Component
public class LogAspect {

  @Autowired(required = false)
  private AuthLogService authLogService;

  /** 频率限制切入点(注解类的路径) */
  @Pointcut(value = "@annotation(com.seepine.secret.annotation.Log)")
  public void logPointCut() {}

  /**
   * 切面请求频率限制
   *
   * @param joinPoint joinPoint
   * @return obj
   * @throws Throwable e
   */
  @Around("logPointCut()")
  public Object doAfter(ProceedingJoinPoint joinPoint) throws Throwable {
    if (authLogService == null) {
      return joinPoint.proceed();
    }
    Signature signature = joinPoint.getSignature();
    MethodSignature methodSignature = (MethodSignature) signature;
    Object result = null;
    Throwable exception = null;
    long startTime = CurrentTimeMillis.now();
    try {
      result = joinPoint.proceed();
    } catch (Throwable e) {
      exception = e;
    }
    Log log = methodSignature.getMethod().getAnnotation(Log.class);
    LogEvent logEvent = LogUtil.gen(log, CurrentTimeMillis.now() - startTime, exception);
    authLogService.save(logEvent);
    if (exception != null) {
      throw exception;
    }
    return result;
  }
}
