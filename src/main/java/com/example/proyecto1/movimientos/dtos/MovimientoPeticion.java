package com.example.proyecto1.movimientos.dtos;

import com.example.proyecto1.movimientos.entities.Moneda;
import com.example.proyecto1.movimientos.entities.TipoMovimiento;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record MovimientoPeticion(
        @NotNull(message = "El monto es obligatorio")
        @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
        @Digits(integer = 10, fraction = 2, message = "El monto tiene un formato invalido (máx 2 decimales)")
        BigDecimal monto,

        @NotNull(message = "La moneda es obligatoria")
        Moneda moneda,

        @NotNull(message = "La categoría es obligatoria")
        Long categoriaId,

        @Size (max = 255, message = "La descripción no puede exceder los 255 caracteres")
        String descripcion,

        @NotNull(message = "El tipo de movimiento (INGRESO o EGRESO) es obligatorio")
        TipoMovimiento tipo,
        @NotNull(message = "La fecha es obligatoria")
        @PastOrPresent(message = "La fecha no puede ser futura")
        LocalDate fecha
){}