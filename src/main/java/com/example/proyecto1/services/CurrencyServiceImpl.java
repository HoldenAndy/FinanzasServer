package com.example.proyecto1.services;

import com.example.proyecto1.models.dtos.ExchangeRateResponse;
import com.example.proyecto1.models.entities.Moneda;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CurrencyServiceImpl implements  CurrencyService{

    @Value("${app.exchangerate.api.key}")
    private String apiKey;

    @Value("${app.exchangerate.base.url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public CurrencyServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    @Transactional
    public BigDecimal convertir(BigDecimal monto, Moneda origen, String destino) {
        if (origen.name().equals(destino)) return monto;

        String url = baseUrl + apiKey + "/latest/" + origen.name();
        try {
            ExchangeRateResponse response = restTemplate.getForObject(url, ExchangeRateResponse.class);
            if (response != null && response.conversion_rates().containsKey(destino)) {
                BigDecimal tasa = response.conversion_rates().get(destino);
                return monto.multiply(tasa).setScale(2, RoundingMode.HALF_UP);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error en conversión: " + e.getMessage());
        }
        return monto;
    }
}