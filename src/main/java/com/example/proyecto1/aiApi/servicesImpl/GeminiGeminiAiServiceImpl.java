package com.example.proyecto1.aiApi.servicesImpl;

import com.example.proyecto1.aiApi.services.GeminiAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

@Service
public class GeminiGeminiAiServiceImpl implements GeminiAiService {

    @Value("${google.ai.api-key}")
    private String apiKey;

    private final RestClient restClient;

    public GeminiGeminiAiServiceImpl(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent")
                .build();
    }

    @Override
    public String obtenerConsejoFinanciero(String contextoFinanciero) {
        String promptSistema = """
                Eres un asesor financiero experto, agresivo pero realista. 
                Tu objetivo es regañar al usuario si gasta mal y felicitarlo si ahorra.
                Dame 3 consejos puntuales (bullets) basados en sus datos.
                Usa emojis. Sé breve.
                """;

        // Estructura JSON que pide Gemini
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", promptSistema + "\n\n" + contextoFinanciero)
                        ))
                )
        );

        try {
            Map<String, Object> response = restClient.post()
                    .uri(uriBuilder -> uriBuilder.queryParam("key", apiKey).build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(Map.class);

            return extraerTextoDeRespuesta(response);

        } catch (Exception e) {
            return "⚠️ La IA está tomando una siesta (Error de conexión): " + e.getMessage();
        }
    }

    private String extraerTextoDeRespuesta(Map<String, Object> response) {
        try {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            return (String) parts.get(0).get("text");
        } catch (Exception e) {
            return "No pude leer el consejo de la IA.";
        }
    }
}