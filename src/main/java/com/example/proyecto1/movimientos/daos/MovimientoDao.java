package com.example.proyecto1.movimientos.daos;

import com.example.proyecto1.movimientos.dtos.PaginacionMovimientos;
import com.example.proyecto1.movimientos.entities.Movimiento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MovimientoDao {
    void saveMovimiento(Movimiento movimiento);
    PaginacionMovimientos<Movimiento> findAllByUsuarioId(Long usuarioId, LocalDate fechaInicio, LocalDate fechaFin, int pagina, int tamanio);

    BigDecimal calcularSaldoTotal(Long usuarioId);
    Optional<Movimiento> findById(Long id);
    void actualizarMovimiento(Movimiento movimiento);
    void eliminarMovimiento(Long id);
}
