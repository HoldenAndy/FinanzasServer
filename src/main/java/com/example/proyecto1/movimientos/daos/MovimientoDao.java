package com.example.proyecto1.movimientos.daos;

import com.example.proyecto1.movimientos.entities.Movimiento;
import com.example.proyecto1.movimientos.entities.TipoMovimiento;
import com.example.proyecto1.reportes.dtos.ReporteCategoriaDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface MovimientoDao {
    void saveMovimiento(Movimiento movimiento);
    List<Movimiento> findAllByUsuarioId(Long usuarioId);
    List<ReporteCategoriaDTO> obtenerDistribucionPorCategoria(Long usuarioId, LocalDate inicio, LocalDate fin, TipoMovimiento tipo);    BigDecimal calcularSaldoTotal(Long usuarioId);
    BigDecimal calcularGastoPorCategoriaYFechas(Long usuarioId, Long categoriaId, LocalDate inicio, LocalDate fin);
}
