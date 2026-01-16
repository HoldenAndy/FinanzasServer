package com.example.proyecto1.movimientos.daos;

import com.example.proyecto1.movimientos.entities.Movimiento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface MovimientoDao {
    void saveMovimiento(Movimiento movimiento);
    List<Movimiento> findAllByUsuarioId(Long usuarioId);
    BigDecimal calcularSaldoTotal(Long usuarioId);
    BigDecimal calcularGastoPorCategoriaYFechas(Long usuarioId, Long categoriaId, LocalDate inicio, LocalDate fin);
}
