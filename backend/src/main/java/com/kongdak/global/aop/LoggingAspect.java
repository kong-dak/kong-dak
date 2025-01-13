package com.kongdak.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    // Controller 로깅
    @Around("execution(* com.kongdak..controller..*.*(..))")
    public Object loggingController(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long end = System.currentTimeMillis();

            log.info("Request: {} ({}) = {} ({}ms)",
                    joinPoint.getSignature().getName(),
                    joinPoint.getArgs(),
                    result,
                    end - start);

            return result;
        } catch (Exception e) {
            log.error("Controller Error: {} ({})",
                    joinPoint.getSignature().getName(),
                    e.getMessage());
            throw e;
        }
    }

    // Service 로깅
    @Around("execution(* com.kongdak..service..*.*(..))")
    public Object loggingService(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long end = System.currentTimeMillis();

            if (end - start > 1000) {  // 1초 이상 걸린 경우만 로깅
                log.warn("Slow Service: {} ({}) = {} ({}ms)",
                        joinPoint.getSignature().getName(),
                        joinPoint.getArgs(),
                        result,
                        end - start);
            }

            return result;
        } catch (Exception e) {
            log.error("Service Error: {} ({})",
                    joinPoint.getSignature().getName(),
                    e.getMessage());
            throw e;
        }
    }
}
