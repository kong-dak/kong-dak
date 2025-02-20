package com.kongdak.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;
import com.kongdak.global.response.BaseResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = extractTokenFromRequest(request);
            
            if(token == null) {
                handleAuthenticationError(response, ErrorCode.UNAUTHORIZED_ACCESS);
                return;
            }
            jwtTokenProvider.validateToken(token);
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);    
        } catch (BusinessException e) {
            log.error("Authentication error occurred: {}", e.getMessage());
            SecurityContextHolder.clearContext();
            handleAuthenticationError(response, e.getErrorCode());
        } catch (Exception e) {
            log.error("Unexpected error occurred during authentication: ", e);
            SecurityContextHolder.clearContext();
            handleAuthenticationError(response, ErrorCode.INTERNAL_SERVER_ERROR);
        }

        
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        boolean shouldNotFilter = path.equals("/api/login") ||
                path.startsWith("/oauth2/") ||
                path.startsWith("/api/auth") ||
                path.startsWith("/api/v3/api-docs") ||
                path.startsWith("/api/swagger-ui/");

        System.out.println("Path: " + path + ", shouldNotFilter = " + shouldNotFilter);
        return shouldNotFilter;
    }

    private void handleAuthenticationError(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        BaseResponse<?> errorResponse = BaseResponse.error(
                errorCode.getStatus(),
                errorCode.getCode(),
                errorCode.getMessage()
        );

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
