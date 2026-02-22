package com.example.proyecto1.aiApi.controllers;

import com.example.proyecto1.aiApi.services.GeminiAiService;
import com.example.proyecto1.aiApi.servicesImpl.FinancialContextServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AiController {

    private final FinancialContextServiceImpl contextService;
    private final GeminiAiService geminiAiService;

    public AiController(FinancialContextServiceImpl contextService, GeminiAiService geminiAiService) {
        this.contextService = contextService;
        this.geminiAiService = geminiAiService;
    }

    @GetMapping("/consejo")
    public ResponseEntity<Map<String, String>> obtenerConsejo(Principal principal) {
        String contexto = contextService.obtenerContextoFinanciero(principal.getName());

        String consejo = geminiAiService.obtenerConsejoFinanciero(contexto);

        return ResponseEntity.ok(Map.of("consejo", consejo));
    }
}