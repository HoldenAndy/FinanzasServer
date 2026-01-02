package com.example.proyecto1.services;

import com.example.proyecto1.models.entities.Moneda;

import java.math.BigDecimal;

public interface CurrencyService {
    BigDecimal convertir(BigDecimal monto, Moneda origen, String destino);
}
