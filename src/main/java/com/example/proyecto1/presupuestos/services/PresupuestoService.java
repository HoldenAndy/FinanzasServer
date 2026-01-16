package com.example.proyecto1.presupuestos.services;

import com.example.proyecto1.presupuestos.dtos.PresupuestoPeticion;
import com.example.proyecto1.presupuestos.dtos.PresupuestoResumen;
import java.util.List;

public interface PresupuestoService {
    void crearPresupuesto(PresupuestoPeticion peticion, String emailUsuario);
    List<PresupuestoResumen> obtenerPresupuestosConEstado(String emailUsuario);
    void eliminarPresupuesto(Long id, String emailUsuario);
}