package com.example.proyecto1.reportes.dtos;

import java.math.BigDecimal;

public record ReporteMensualDTO(
        String etiqueta,
        BigDecimal ingresos,
        BigDecimal egresos
) {}