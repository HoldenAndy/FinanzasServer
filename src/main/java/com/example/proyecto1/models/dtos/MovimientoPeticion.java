package com.example.proyecto1.models.dtos;

import com.example.proyecto1.models.entities.TipoMovimiento;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record MovimientoPeticion(
        @NotNull(message = "El monto es obligatorio")
        @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
        BigDecimal monto,

        @NotNull(message = "La categoría es obligatoria")
        Long categoriaId,

        String descripcion,
        TipoMovimiento tipo,
        LocalDate fecha
){}