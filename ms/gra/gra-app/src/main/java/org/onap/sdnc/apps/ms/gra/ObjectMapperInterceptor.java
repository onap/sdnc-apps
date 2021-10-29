package org.onap.sdnc.apps.ms.gra;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.ReflectionUtils;

public abstract class ObjectMapperInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
      return ReflectionUtils.invokeMethod(invocation.getMethod(), getObject(), invocation.getArguments());
    } 
  
    protected abstract ObjectMapper getObject();
  
}



