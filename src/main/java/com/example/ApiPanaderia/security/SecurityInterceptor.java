package com.example.ApiPanaderia.security;

import com.example.ApiPanaderia.model.TokenSession;
import com.example.ApiPanaderia.repository.TokenSessionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@Component
public class SecurityInterceptor implements HandlerInterceptor {

    private final TokenSessionRepository tokenSessionRepository;

    public SecurityInterceptor(TokenSessionRepository tokenSessionRepository) {
        this.tokenSessionRepository = tokenSessionRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        
        RequireRole requireRole = handlerMethod.getMethodAnnotation(RequireRole.class);
        if (requireRole == null) {
            requireRole = handlerMethod.getBeanType().getAnnotation(RequireRole.class);
        }

        if (requireRole == null) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"Acceso no autorizado. Se requiere token Bearer.\"}");
            return false;
        }

        String token = authHeader.substring(7).trim();
        Optional<TokenSession> sessionOpt = tokenSessionRepository.findById(token);

        if (sessionOpt.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"Token inválido o inexistente.\"}");
            return false;
        }

        TokenSession session = sessionOpt.get();

        if (session.getFechaExpiracion().before(new java.util.Date())) {
            tokenSessionRepository.delete(session);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"El token ha expirado. Inicie sesión de nuevo.\"}");
            return false;
        }

        String[] allowedRoles = requireRole.value();
        if (allowedRoles.length > 0) {
            boolean hasAccess = Arrays.asList(allowedRoles).contains(session.getRol());
            if (!hasAccess) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"error\": \"Acceso denegado. No tiene permisos suficientes.\"}");
                return false;
            }
        }

        request.setAttribute("username", session.getUsername());
        request.setAttribute("rol", session.getRol());

        return true;
    }
}
