package com.example.proyecto1.categorias.dtos;

import com.example.proyecto1.movimientos.entities.TipoMovimiento;

public record CategoriaPeticion(
        String nombre,
        TipoMovimiento tipo
) {}