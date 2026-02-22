package com.example.proyecto1.movimientos.daos;

import com.example.proyecto1.movimientos.entities.Movimiento;
import com.example.proyecto1.movimientos.entities.TipoMovimiento;
import com.example.proyecto1.reportes.dtos.ReporteBalanceDTO;
import com.example.proyecto1.reportes.dtos.ReporteCategoriaDTO;
import com.example.proyecto1.reportes.dtos.ReporteMensualDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface MovimientoDao {
    void saveMovimiento(Movimiento movimiento);
    boolean eliminarMovimiento(Long id, Long usuarioId);
    List<Movimiento> findAllByUsuarioId(Long usuarioId);
    BigDecimal calcularSaldoTotal(Long usuarioId);
    BigDecimal calcularGastoPorCategoriaYFechas(Long usuarioId, Long categoriaId, LocalDate inicio, LocalDate fin);
    List<ReporteCategoriaDTO> obtenerDistribucionPorCategoria(Long usuarioId, LocalDate inicio, LocalDate fin, TipoMovimiento tipo);
    ReporteBalanceDTO obtenerBalanceGeneral(Long usuarioId, LocalDate inicio, LocalDate fin);
    List<ReporteMensualDTO> obtenerHistorialUltimos6Meses(Long usuarioId);
    List<ReporteMensualDTO> obtenerGastosDiarios(Long usuarioId, int anio, int mes);
}
