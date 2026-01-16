package com.example.proyecto1.presupuestos.daos;

import com.example.proyecto1.presupuestos.entities.Presupuesto;
import java.util.List;
import java.util.Optional;

public interface PresupuestoDao {

    void crearPresupuesto(Presupuesto p);

    List<Presupuesto> listarPorUsuario(Long usuarioId);

    Optional<Presupuesto> buscarPorIdAndUsuario(Long id, Long usuarioId);

    void eliminarPresupuesto(Long id);

    boolean existePresupuestoEnFechas(Long usuarioId, Long categoriaId, java.time.LocalDate inicio, java.time.LocalDate fin);

}