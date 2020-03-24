package org.wyw.lanplay.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author wuyd
 * 创建时间：2019/10/23 15:38
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * 切点
     */
    @Pointcut("@annotation(org.wyw.lanplay.aop.Log)")
    public void controllerAspect() { }


    /**
     * 切面
     *
     */
    @SuppressWarnings("all")
    @Before("controllerAspect()")
    public void before(JoinPoint point) throws Throwable,NullPointerException {

        startTime.set(System.currentTimeMillis());

        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.info("DESCRIPTION : {}",getMethodDescription(point));
        log.info("URL : {}",request.getRequestURL().toString());
        log.info("IP : {}",request.getRemoteAddr());
        log.info("CLASS_METHOD : {}.{}", point.getSignature().getDeclaringTypeName(),point.getSignature().getName());
        log.info("ARGS : {}" + Arrays.toString(point.getArgs()));
    }

    @AfterReturning(returning = "ret", pointcut = "controllerAspect()")
    public void doAfterReturning(Object ret){
        //处理完返回内容
        log.info("RESPONSE : {}", ret);
        log.info("SPEND TIME : {}", (System.currentTimeMillis() - startTime.get()));
        startTime.remove();
    }

    /**
     * 获取方法中的中文备注
     *
     * @param joinPoint 切点
     */
    private static String getMethodDescription(JoinPoint joinPoint) throws Exception {

        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();

        Class targetClass = Class.forName(targetName);
        Method[] method = targetClass.getMethods();
        String methode = "";
        for (Method m : method) {
            if (m.getName().equals(methodName)) {
                Class[] tmpCs = m.getParameterTypes();
                if (tmpCs.length == arguments.length) {
                    Log methodCache = m.getAnnotation(Log.class);
                    if (methodCache != null) {
                        methode = methodCache.description();
                    }
                    break;
                }
            }
        }
        return methode;
    }

}
