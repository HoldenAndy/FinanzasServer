package com.example.proyecto1.reportes.controllers;

import com.example.proyecto1.movimientos.entities.TipoMovimiento;
import com.example.proyecto1.reportes.dtos.ReporteBalanceDTO;
import com.example.proyecto1.reportes.dtos.ReporteCategoriaDTO;
import com.example.proyecto1.reportes.dtos.ReporteMensualDTO;
import com.example.proyecto1.reportes.services.ReporteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    public final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/distribucion-categoria")
    public ResponseEntity<List<ReporteCategoriaDTO>> obtenerDistribucion(
            @RequestParam(required = false) LocalDate fechaInicio,
            @RequestParam(required = false) LocalDate fechaFin,
            @RequestParam(defaultValue = "EGRESO") TipoMovimiento tipo,
            Principal principal) {

        return ResponseEntity.ok(reporteService.obtenerDistribucion(
                principal.getName(),
                fechaInicio,
                fechaFin,
                tipo
        ));
    }

    @GetMapping("/balance")
    public ResponseEntity<ReporteBalanceDTO> obtenerBalance(
            @RequestParam(required = false) LocalDate fechaInicio,
            @RequestParam(required = false) LocalDate fechaFin,
            Principal principal) {
        return ResponseEntity.ok(reporteService.obtenerBalance(principal.getName(), fechaInicio, fechaFin));
    }

    @GetMapping("/historico")
    public ResponseEntity<List<ReporteMensualDTO>> obtenerHistorial(Principal principal) {
        return ResponseEntity.ok(reporteService.obtenerHistorial(principal.getName()));
    }

    @GetMapping("/diario")
    public ResponseEntity<List<ReporteMensualDTO>> obtenerDiario(
            @RequestParam int anio,
            @RequestParam int mes,
            Principal principal) {
        return ResponseEntity.ok(reporteService.obtenerDiario(principal.getName(), anio, mes));
    }

}
