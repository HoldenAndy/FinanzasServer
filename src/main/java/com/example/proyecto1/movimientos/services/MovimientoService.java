package com.example.proyecto1.movimientos.services;

import com.example.proyecto1.movimientos.dtos.MovimientoPeticion;
import com.example.proyecto1.movimientos.entities.Movimiento;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface MovimientoService {
    void crearMovimiento(MovimientoPeticion request, String emailUsuario);
    List<Movimiento> listarMisMovimientos(String emailUsuario);
    Map<String, Object> obtenerResumenFinanciero(String email);
    void eliminarMovimiento(Long id, String email);
}
