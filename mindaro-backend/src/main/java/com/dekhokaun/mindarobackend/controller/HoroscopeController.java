package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.service.HoroscopeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/horoscope")
@Tag(name = "Horoscope", description = "Horoscope APIs")
public class HoroscopeController {

    private final HoroscopeService horoscopeService;

    public HoroscopeController(HoroscopeService horoscopeService) {
        this.horoscopeService = horoscopeService;
    }

    @Operation(summary = "Daily horoscope")
    @GetMapping("/daily")
    public ResponseEntity<Map<String, Object>> daily(@RequestParam String sign) {
        return ResponseEntity.ok(horoscopeService.daily(sign));
    }

    @Operation(summary = "Weekly horoscope")
    @GetMapping("/weekly")
    public ResponseEntity<Map<String, Object>> weekly(@RequestParam String sign) {
        return ResponseEntity.ok(horoscopeService.weekly(sign));
    }
}
