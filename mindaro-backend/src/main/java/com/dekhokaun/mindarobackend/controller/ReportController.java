package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports", description = "Reporting APIs")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(summary = "Usage report")
    @GetMapping("/usage")
    public ResponseEntity<Map<String, Object>> usage() {
        return ResponseEntity.ok(reportService.usage());
    }
}
