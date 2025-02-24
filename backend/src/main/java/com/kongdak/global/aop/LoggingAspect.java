package com.kongdak.global.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    // Controller 레벨 로깅
    @Around("execution(* com.kongdak..controller..*.*(..))")
    public Object loggingController(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String httpMethod = request.getMethod();
        String uri = request.getRequestURI();
        String params = getParameterDetails(joinPoint);

        log.info("[API] {} {} - request: {}", httpMethod, uri, params);

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;

            log.info("[API] {} {} - response: {} ({}ms)",
                    httpMethod, uri, result, executionTime);
            return result;
        } catch (Exception e) {
            log.error("[API] {} {} - error: {}",
                    httpMethod, uri, e.getMessage(), e);
            throw e;
        }
    }

    // Security 레벨 로깅
    @Around("execution(* com.kongdak..security..*.*(..))")
    public Object loggingSecurity(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String params = getParameterDetails(joinPoint);

        log.info("[Auth] {}.{} start - params: {}", className, methodName, params);

        try {
            Object result = joinPoint.proceed();
            log.info("[Auth] {}.{} success - result: {}",
                    className, methodName, maskSensitiveData(result));
            return result;
        } catch (Exception e) {
            log.error("[Auth] {}.{} failed - cause: {}",
                    className, methodName, e.getMessage(), e);
            throw e;
        }
    }

    // Service 레벨 로깅
    @Around("execution(* com.kongdak..service..*.*(..))")
    public Object loggingService(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String params = getParameterDetails(joinPoint);

        log.info("[Service] {}.{} start - params: {}",
                className, methodName, params);

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;

            log.info("[Service] {}.{} end - result: {} ({}ms)",
                    className, methodName, result, executionTime);
            return result;
        } catch (Exception e) {
            log.error("[Service] {}.{} error - cause: {}",
                    className, methodName, e.getMessage(), e);
            throw e;
        }
    }

    // Repository 레벨 로깅
    @Around("execution(* com.kongdak..repository..*.*(..))")
    public Object loggingRepository(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String params = getParameterDetails(joinPoint);

        log.debug("[DB] {}.{} start - params: {}",
                className, methodName, params);

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;

            // 실행시간이 특정 임계값을 넘을 경우에만 info 레벨로 로깅
            if (executionTime > 500) { // 500ms
                log.info("[DB] {}.{} slow query - ({}ms)",
                        className, methodName, executionTime);
            }

            log.debug("[DB] {}.{} end - result: {} ({}ms)",
                    className, methodName, result, executionTime);
            return result;
        } catch (Exception e) {
            log.error("[DB] {}.{} error - cause: {}",
                    className, methodName, e.getMessage(), e);
            throw e;
        }
    }

    // SSE/알림 레벨 로깅
    @Around("execution(* com.kongdak..notification..*.*(..))")
    public Object loggingNotification(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String params = getParameterDetails(joinPoint);

        log.info("[Notification] {}.{} start - params: {}",
                className, methodName, params);

        try {
            Object result = joinPoint.proceed();
            log.info("[Notification] {}.{} success - to: {}",
                    className, methodName, params);
            return result;
        } catch (Exception e) {
            log.error("[Notification] {}.{} failed - cause: {}",
                    className, methodName, e.getMessage(), e);
            throw e;
        }
    }

    private String getParameterDetails(ProceedingJoinPoint joinPoint) {
        try {
            Object[] args = joinPoint.getArgs();
            if (args == null || args.length == 0) {
                return "없음";
            }

            Map<String, Object> paramMap = new LinkedHashMap<>();
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String[] parameterNames = signature.getParameterNames();
            Class<?>[] parameterTypes = signature.getParameterTypes();

            for (int i = 0; i < args.length; i++) {
                Object value = args[i];
                String paramName = parameterNames != null && parameterNames.length > i ?
                        parameterNames[i] : parameterTypes[i].getSimpleName() + "_" + i;

                if (value == null) {
                    paramMap.put(paramName, null);
                    continue;
                }

                if (value instanceof MultipartFile) {
                    MultipartFile file = (MultipartFile) value;
                    paramMap.put(paramName, String.format("파일명: %s, 크기: %d bytes",
                            file.getOriginalFilename(), file.getSize()));
                } else if (value instanceof HttpServletRequest ||
                        value instanceof HttpServletResponse) {
                    continue;
                } else {
                    paramMap.put(paramName, maskSensitiveData(value));
                }
            }

            return objectMapper.writeValueAsString(paramMap);
        } catch (Exception e) {
            log.warn("[System] 파라미터 변환 실패 - cause: {}", e.getMessage());
            return "파라미터 변환 실패";
        }
    }

    private Object maskSensitiveData(Object data) {
        if (data == null) return null;

        String stringValue = data.toString();
        // 민감정보 마스킹 처리
        stringValue = stringValue.replaceAll("\"password\":\\s*\"[^\"]*\"", "\"password\":\"***\"");
        stringValue = stringValue.replaceAll("\"token\":\\s*\"[^\"]*\"", "\"token\":\"***\"");
        stringValue = stringValue.replaceAll("\"refreshToken\":\\s*\"[^\"]*\"", "\"refreshToken\":\"***\"");
        stringValue = stringValue.replaceAll("\"accessToken\":\\s*\"[^\"]*\"", "\"accessToken\":\"***\"");

        return stringValue;
    }
}