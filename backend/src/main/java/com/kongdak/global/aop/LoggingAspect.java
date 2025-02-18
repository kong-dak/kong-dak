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
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule()); // Java 8 시간 모듈 등록

//    // 민감한 데이터 마스킹 처리를 위한 상수
//    private static final List<String> SENSITIVE_FIELDS = Arrays.asList(
//            "password", "token", "accessToken", "refreshToken", "email"
//    );

    @Around("execution(* com.kongdak..controller..*.*(..))")
    public Object loggingController(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        // 파라미터 반환
        String params = getParameterDetails(joinPoint);

        log.info("[시작] {}.{} 요청 파라미터: {}", className, methodName, params);

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;

//            // 응답 데이터 마스킹 처리
//            String maskedResult = maskSensitiveData(result);

            log.info("[완료] {}.{} 실행시간: {}ms, 응답: {}",
                    className, methodName, executionTime, result);

            return result;
        } catch (Exception e) {
            log.error("[에러] {}.{} 에러 메시지: {}",
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
            Class<?>[] parameterTypes = signature.getParameterTypes();

            for (int i = 0; i < args.length; i++) {
                Object value = args[i];
                String paramInfo = String.format("%s_%d", parameterTypes[i].getSimpleName(), i);

                if (value == null) {
                    paramMap.put(paramInfo, null);
                    continue;
                }

                if (value instanceof MultipartFile) {
                    MultipartFile file = (MultipartFile) value;
                    paramMap.put(paramInfo, String.format("파일명: %s, 크기: %d bytes",
                            file.getOriginalFilename(), file.getSize()));
                } else if (value instanceof HttpServletRequest ||
                        value instanceof HttpServletResponse) {
                    continue;
                } else {
                    paramMap.put(paramInfo, value);
                }
            }

            return objectMapper.writeValueAsString(paramMap);
        } catch (Exception e) {
            log.warn("파라미터 변환 중 에러 발생", e);
            return "파라미터 변환 실패";
        }
    }

//    // 민감 정보 마스킹 처리
//    private String maskSensitiveData(Object data) {
//        if (data == null) return "null";
//
//        String stringData = data.toString();
//        for (String field : SENSITIVE_FIELDS) {
//            stringData = stringData.replaceAll(
//                    String.format("\"%s\":\\s*\"[^\"]*\"", field),
//                    String.format("\"%s\":\"***\"", field)
//            );
//        }
//        return stringData;
//    }
}