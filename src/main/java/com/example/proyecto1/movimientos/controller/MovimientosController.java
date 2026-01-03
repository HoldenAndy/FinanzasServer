package com.example.proyecto1.movimientos.controller;

import com.example.proyecto1.movimientos.dtos.MovimientoPeticion;
import com.example.proyecto1.movimientos.dtos.PaginacionMovimientos;
import com.example.proyecto1.movimientos.entities.Movimiento;
import com.example.proyecto1.movimientos.services.MovimientoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/movimientos")

public class MovimientosController {
    private final MovimientoService movimientoService;

    public MovimientosController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @PostMapping
    public ResponseEntity<?> crearMovimiento(@Valid @RequestBody MovimientoPeticion request, Principal principal) {
        movimientoService.crearMovimiento(request, principal.getName());
        return ResponseEntity.ok(Map.of("mensaje", "Movimiento registrado con éxito"));
    }

    @GetMapping
    public ResponseEntity<PaginacionMovimientos<Movimiento>> listarMovimientos(
            @RequestParam(required = false) LocalDate fechaInicio,
            @RequestParam(required = false) LocalDate fechaFin,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamanio,
            Principal principal) {

        PaginacionMovimientos<Movimiento> respuesta = movimientoService.listarMisMovimientos(
                principal.getName(),
                fechaInicio,
                fechaFin,
                pagina,
                tamanio
        );

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/balance")
    public ResponseEntity<?> obtenerBalance(Principal principal) {
        // Usamos el email del token para saber de quién es el balance
        return ResponseEntity.ok(movimientoService.obtenerResumenFinanciero(principal.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizarMovimiento(
            @PathVariable Long id,
            @RequestBody MovimientoPeticion peticion,
            Principal principal) {

        movimientoService.editarMovimiento(id, peticion, principal.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMovimiento(
            @PathVariable Long id,
            Principal principal) {

        movimientoService.eliminarMovimiento(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

}
