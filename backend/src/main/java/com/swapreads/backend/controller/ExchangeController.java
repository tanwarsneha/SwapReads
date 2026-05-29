package com.swapreads.backend.controller;

import com.swapreads.backend.dto.ExchangeRequestDTO;
import com.swapreads.backend.dto.ExchangeResponseDTO;
import com.swapreads.backend.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/exchange")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class ExchangeController {

    private final ExchangeService exchangeService;

    @PostMapping("/request")
    public ResponseEntity<ExchangeResponseDTO> createRequest(
            @RequestBody ExchangeRequestDTO dto,
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(
                exchangeService.createRequest(dto, email));
    }

    @PutMapping("/respond/{requestId}")
    public ResponseEntity<ExchangeResponseDTO> respondToRequest(
            @PathVariable Long requestId,
            @RequestParam String action,
            @RequestParam(required = false)
            String responseMessage,
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(
                exchangeService.respondToRequest(
                        requestId, action, responseMessage, email));
    }

    @PutMapping("/cancel/{requestId}")
    public ResponseEntity<ExchangeResponseDTO> cancelRequest(
            @PathVariable Long requestId,
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(
                exchangeService.cancelRequest(requestId, email));
    }

    @GetMapping("/my-requests")
    public ResponseEntity<List<ExchangeResponseDTO>>
    getMyRequests(
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(
                exchangeService.getMyRequests(email));
    }

    @GetMapping("/incoming")
    public ResponseEntity<List<ExchangeResponseDTO>>
    getIncomingRequests(
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(
                exchangeService.getIncomingRequests(email));
    }
}