package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.model.CreditBalance;
import com.dekhokaun.mindarobackend.payload.request.CreditsPurchaseRequest;
import com.dekhokaun.mindarobackend.payload.response.CreditsPurchaseResponse;
import com.dekhokaun.mindarobackend.service.CreditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/credits")
@Tag(name = "Credits", description = "Credits purchase APIs")
@RequiredArgsConstructor
public class CreditsController {

    private final CreditService creditService;

    @Operation(summary = "Buy credits", description = "Purchases credits for a user and records credit & wallet transactions")
    @PostMapping("/purchase")
    public ResponseEntity<CreditsPurchaseResponse> purchase(@Valid @RequestBody CreditsPurchaseRequest request) {
        CreditBalance balance = creditService.purchase(request);
        return ResponseEntity.ok(CreditsPurchaseResponse.from(balance));
    }
}
