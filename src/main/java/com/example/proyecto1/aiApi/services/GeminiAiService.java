package com.example.proyecto1.aiApi.services;

import org.springframework.stereotype.Repository;

@Repository
public interface GeminiAiService {
    String obtenerConsejoFinanciero(String contextoFinanciero);
}