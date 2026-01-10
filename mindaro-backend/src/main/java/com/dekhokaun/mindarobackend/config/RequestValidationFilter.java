package com.dekhokaun.mindarobackend.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
public class RequestValidationFilter implements Filter {
    
    private static final Logger logger = LoggerFactory.getLogger(RequestValidationFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String method = httpRequest.getMethod();
            
            // Log suspicious requests
            if (method == null || method.trim().isEmpty() || method.length() > 10) {
                logger.warn("Suspicious HTTP method received: '{}' from {}", method, httpRequest.getRemoteAddr());
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                httpResponse.getWriter().write("Invalid HTTP method");
                return;
            }
            
            // Check for common malformed request patterns
            String requestURI = httpRequest.getRequestURI();
            if (requestURI != null && (requestURI.contains("\0") || requestURI.length() > 2048)) {
                logger.warn("Suspicious request URI received: '{}' from {}", requestURI, httpRequest.getRemoteAddr());
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                httpResponse.getWriter().write("Invalid request URI");
                return;
            }
        }
        
        chain.doFilter(request, response);
    }
} 