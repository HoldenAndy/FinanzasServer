package com.example.proyecto1.categorias.dtos;

import com.example.proyecto1.movimientos.entities.TipoMovimiento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoriaPeticion(
    @NotBlank(message = "El nombre de la categoria es obligatorio")
    String nombre,

    @NotNull(message = "El tipo (INGRESO o EGRESO) es obligatorio")
    TipoMovimiento tipo
) {}
