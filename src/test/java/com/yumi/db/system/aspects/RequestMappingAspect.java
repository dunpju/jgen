package com.yumi.db.system.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.reflect.Method;
import java.text.ParseException;

// 参考资料：https://blog.csdn.net/lx_nhs/article/details/73571717
@Component
@Aspect
@Order(1)
public class RequestMappingAspect {
    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void pointcut() {
        System.out.println("pointCut签名。。。");
    }

    @Before("pointcut()")
    public void beforeExecute(JoinPoint joinPoint) throws NoSuchMethodException, ParseException {
        Object target = joinPoint.getTarget();
        MethodSignature methdSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methdSignature.getMethod();
        System.out.println(method);
        for (String s : method.getAnnotation(GetMapping.class).value()) {
            System.out.println(s);
        }

        System.out.println("beforeExecute。。。");
    }
}
