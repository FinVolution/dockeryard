package com.ppdai.dockeryard.admin.aop;

import org.aspectj.lang.annotation.Pointcut;

public class ResourcePointCuts {

    @Pointcut("execution(public * com.ppdai.dockeryard.admin.controller..*.*(..))")
    public void apiController() {
    }

}
