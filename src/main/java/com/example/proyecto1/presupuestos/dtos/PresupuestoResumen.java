package com.example.proyecto1.presupuestos.dtos;

import com.example.proyecto1.presupuestos.entities.EstadoPresupuesto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PresupuestoResumen(
        Long id,
        String categoria,
        BigDecimal montoLimite,
        BigDecimal montoGastado,
        String moneda,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        BigDecimal porcentajeConsumido,
        EstadoPresupuesto estado
) {}