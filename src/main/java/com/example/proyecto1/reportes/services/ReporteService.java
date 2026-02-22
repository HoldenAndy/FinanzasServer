package com.example.proyecto1.reportes.services;

import com.example.proyecto1.movimientos.entities.TipoMovimiento;
import com.example.proyecto1.reportes.dtos.ReporteBalanceDTO;
import com.example.proyecto1.reportes.dtos.ReporteCategoriaDTO;
import com.example.proyecto1.reportes.dtos.ReporteMensualDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

public interface ReporteService {
    public List<ReporteCategoriaDTO> obtenerDistribucion(String email, LocalDate inicio, LocalDate fin, TipoMovimiento tipo);
    ReporteBalanceDTO obtenerBalance(String email, LocalDate inicio, LocalDate fin);
    List<ReporteMensualDTO> obtenerHistorial(String email);
    List<ReporteMensualDTO> obtenerDiario(String email, int anio, int mes);
}