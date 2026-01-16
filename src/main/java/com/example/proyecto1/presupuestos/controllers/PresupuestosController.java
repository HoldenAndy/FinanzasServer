package com.example.proyecto1.presupuestos.controllers;

import com.example.proyecto1.presupuestos.dtos.PresupuestoPeticion;
import com.example.proyecto1.presupuestos.services.PresupuestoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/presupuestos")
public class PresupuestosController {

    private final PresupuestoService presupuestoService;

    public PresupuestosController(PresupuestoService presupuestoService) {
        this.presupuestoService = presupuestoService;
    }

    @PostMapping
    public ResponseEntity<?> crearPresupuesto(@Valid @RequestBody PresupuestoPeticion peticion, Principal principal) {
        presupuestoService.crearPresupuesto(peticion, principal.getName());
        return ResponseEntity.ok(Map.of("mensaje", "Presupuesto creado con éxito"));
    }

    @GetMapping
    public ResponseEntity<?> listarPresupuesto(Principal principal) {
        return ResponseEntity.ok(presupuestoService.obtenerPresupuestosConEstado(principal.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPresupuesto(@PathVariable Long id, Principal principal) {
        presupuestoService.eliminarPresupuesto(id, principal.getName());
        return ResponseEntity.ok(Map.of("mensaje", "Presupuesto eliminado"));
    }
}