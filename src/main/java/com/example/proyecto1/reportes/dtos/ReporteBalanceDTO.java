package com.example.proyecto1.reportes.dtos;

import java.math.BigDecimal;

public record ReporteBalanceDTO(
        BigDecimal totalIngresos,
        BigDecimal totalEgresos,
        BigDecimal balanceTotal, // Ingresos - Egresos
        BigDecimal ahorroPorcentaje // Cuánto del ingreso lograste no gastar
) {}