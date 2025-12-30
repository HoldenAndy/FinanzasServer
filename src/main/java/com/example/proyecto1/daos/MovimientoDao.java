package com.example.proyecto1.daos;

import com.example.proyecto1.models.entities.Movimiento;

import java.math.BigDecimal;
import java.util.List;

public interface MovimientoDao {
    void saveMovimiento(Movimiento movimiento);
    List<Movimiento> findAllByUsuarioId(Long usuarioId);
    BigDecimal calcularSaldoTotal(Long usuarioId);
}
