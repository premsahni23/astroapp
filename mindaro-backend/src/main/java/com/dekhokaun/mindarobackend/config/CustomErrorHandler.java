package com.dekhokaun.mindarobackend.config;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CustomErrorHandler implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        
        if (statusCode == null) {
            statusCode = 500;
        }
        
        errorDetails.put("status", statusCode);
        errorDetails.put("error", HttpStatus.valueOf(statusCode).getReasonPhrase());
        errorDetails.put("message", "An error occurred while processing your request");
        errorDetails.put("path", request.getAttribute("javax.servlet.error.request_uri"));
        
        if (exception != null) {
            errorDetails.put("exception", exception.getClass().getSimpleName());
        }
        
        return ResponseEntity.status(statusCode).body(errorDetails);
    }
} 