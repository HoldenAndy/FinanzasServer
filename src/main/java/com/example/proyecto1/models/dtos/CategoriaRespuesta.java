package com.example.proyecto1.models.dtos;

import com.example.proyecto1.models.entities.TipoMovimiento;

public record CategoriaRespuesta(
        Long id,
        String nombre,
        TipoMovimiento tipo
) {}