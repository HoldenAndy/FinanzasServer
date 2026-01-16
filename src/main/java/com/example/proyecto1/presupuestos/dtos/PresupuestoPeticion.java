package com.example.proyecto1.presupuestos.dtos;

import com.example.proyecto1.movimientos.entities.Moneda;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record PresupuestoPeticion(
        @NotNull(message = "La categoría es obligatoria")
        Long categoriaId,

        @NotNull(message = "El monto límite es obligatorio")
        @DecimalMin(value = "0.01", message = "El límite debe ser mayor a cero")
        BigDecimal montoLimite,

        @NotNull(message = "La fecha de inicio es obligatoria")
        LocalDate fechaInicio,

        @NotNull(message = "La fecha de fin es obligatoria")
        LocalDate fechaFin,

        @NotNull(message = "La moneda es obligatoria")
        Moneda moneda
) {
}