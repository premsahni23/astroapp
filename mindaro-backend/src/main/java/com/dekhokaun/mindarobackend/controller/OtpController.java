package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.payload.request.OtpSendRequest;
import com.dekhokaun.mindarobackend.payload.request.OtpVerifyRequest;
import com.dekhokaun.mindarobackend.payload.response.OtpResponse;
import com.dekhokaun.mindarobackend.service.OtpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/otp")
@Tag(name = "OTP Controller", description = "APIs for sending and verifying OTPs")
public class OtpController {

    private final OtpService otpService;

    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    @Operation(summary = "Send an OTP", description = "Generates and sends an OTP to the user")
    @PostMapping("/send")
    public ResponseEntity<OtpResponse> sendOtp(@RequestBody OtpSendRequest request, HttpServletRequest httpRequest) {
        request.setIp(httpRequest.getRemoteAddr());
        return ResponseEntity.ok(otpService.generateOtp(request));
    }

    @Operation(summary = "Verify an OTP", description = "Verifies the OTP provided by the user")
    @PostMapping("/verify")
    public ResponseEntity<OtpResponse> verifyOtp(@RequestBody OtpVerifyRequest request) {
        return ResponseEntity.ok(otpService.verifyOtp(request));
    }
}
