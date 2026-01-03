package com.example.proyecto1.movimientos.services;

import com.example.proyecto1.movimientos.dtos.MovimientoPeticion;
import com.example.proyecto1.movimientos.dtos.PaginacionMovimientos;
import com.example.proyecto1.movimientos.entities.Movimiento;
import java.time.LocalDate;
import java.util.Map;

public interface MovimientoService {
    void crearMovimiento(MovimientoPeticion request, String emailUsuario);
    PaginacionMovimientos<Movimiento> listarMisMovimientos(String emailUsuario, LocalDate fechaInicio, LocalDate fechaFin, int pagina, int tamanio);
    Map<String, Object> obtenerResumenFinanciero(String email);
    void editarMovimiento(Long id, MovimientoPeticion peticion, String emailUsuario);
    void eliminarMovimiento(Long id, String emailUsuario);
}
