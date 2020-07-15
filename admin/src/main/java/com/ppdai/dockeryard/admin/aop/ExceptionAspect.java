package com.ppdai.dockeryard.admin.aop;

import com.ppdai.dockeryard.admin.util.ResponseUtil;
import com.ppdai.dockeryard.admin.util.ShortUUID;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class ExceptionAspect {

    @Around("ResourcePointCuts.apiController()")
    public Object handleWebException(ProceedingJoinPoint pjp) {
        Object retVal;
        String methodName = null;
        Object[] params = null;
        String shortUuid = ShortUUID.generateShortUuid();
        try {
            Signature sig = pjp.getSignature();
            MethodSignature methodSignature = (MethodSignature) sig;
            Object target = pjp.getTarget();
            //获取当前方法
            Method currentMethod = target.getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
            //当前方法名称
            methodName = currentMethod.getName();
            //当前方法参数
            params = pjp.getArgs();
            retVal = pjp.proceed();
            log.info(String.format("[%s] : the method %s has been called :param is %s", shortUuid, methodName , params));
        } catch (Throwable throwable) {
            log.error(String.format("[%s] error: %s param is %s", shortUuid, methodName , params), throwable);
            retVal = ResponseUtil.createExceptionResponseEntity(throwable, shortUuid);
        }
        return retVal;
    }

}
