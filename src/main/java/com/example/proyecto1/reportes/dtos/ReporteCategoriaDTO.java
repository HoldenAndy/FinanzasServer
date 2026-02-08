package com.example.proyecto1.reportes.dtos;

import java.math.BigDecimal;

public record ReporteCategoriaDTO(
        String categoria,
        BigDecimal totalGastado,
        BigDecimal porcentajeDelTotal // Ej: 30%
) {}