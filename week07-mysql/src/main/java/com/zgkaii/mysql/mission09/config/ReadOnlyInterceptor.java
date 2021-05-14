package com.zgkaii.mysql.mission09.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/05/13 16:04
 * @Description:
 **/
@Aspect
@Component
public class ReadOnlyInterceptor implements Ordered {
    private static final Logger log = LoggerFactory.getLogger(ReadOnlyInterceptor.class);

    @Around("@annotation(readOnly)")
    public Object setRead(ProceedingJoinPoint joinPoint, ReadOnly readOnly) throws Throwable {
        try {
            DbContextHolder.setDbType(DbContextHolder.READ);
            return joinPoint.proceed();
        } finally {
            DbContextHolder.clearDbType();
            log.info("清除ThreadLocal");
        }
    }

    /**
     * getOrder 保证本切面优先级高于事务切面优先级
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
