package com.admin.tool.common.aop.aspect;

import com.admin.tool.common.aop.annotation.LookupKey;
import com.admin.tool.common.aop.annotation.RoutingMapper;
import com.admin.tool.common.config.db.ThreadLocalContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Slf4j
@Aspect
@Configuration
public class RoutingMapperAspect {

    @Around("execution(* com.admin.tool.api..dao.*Mapper.*(..))")
    public Object aroundTargetMethod(ProceedingJoinPoint thisJoinPoint) {
        MethodSignature methodSignature = (MethodSignature) thisJoinPoint.getSignature();
        Class<?> mapperInterface = methodSignature.getDeclaringType();

        RoutingMapper routingMapper = mapperInterface.getDeclaredAnnotation(RoutingMapper.class);
        if (routingMapper != null) {
            String lookUpKey;
            if (!"".equals(routingMapper.fixedLookupKey())) {
                lookUpKey = routingMapper.fixedLookupKey();
            } else {
                Method method = methodSignature.getMethod();
                Parameter[] parameters = method.getParameters();
                Object[] args = thisJoinPoint.getArgs();

                lookUpKey = findLookupKey(parameters, args);
            }

            if (log.isDebugEnabled())
                log.info("Route Key: {}", chooseMasterOrSlave(lookUpKey));

            ThreadLocalContext.set(chooseMasterOrSlave(lookUpKey));
        }

        try {
            return thisJoinPoint.proceed();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        } finally {
            ThreadLocalContext.remove();
        }
    }

    private String findLookupKey(Parameter[] parameters, Object[] args) {
        for (int i = 0; i < parameters.length; i++) {
            LookupKey lookupKey = parameters[i].getDeclaredAnnotation(LookupKey.class);
            if (lookupKey != null) {
                return String.valueOf(args[i]);
            }
        }
        throw new RuntimeException("can't find LookupKey");
    }

    private String chooseMasterOrSlave(String lookUp) {
        boolean isReadOnly = TransactionSynchronizationManager.isActualTransactionActive() &&
                TransactionSynchronizationManager.isCurrentTransactionReadOnly();

        return isReadOnly ? lookUp + "_slave" : lookUp;
    }
}
