package com.example.proyecto1.services;

import com.example.proyecto1.daos.MovimientoDao;
import com.example.proyecto1.daos.UsuarioDao;
import com.example.proyecto1.models.dtos.MovimientoPeticion;
import com.example.proyecto1.models.entities.Movimiento;
import com.example.proyecto1.models.entities.Usuario;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MovimientoServiceImpl implements MovimientoService{

    private final MovimientoDao movimientoDao;
    private final UsuarioDao usuarioDao;

    public MovimientoServiceImpl(MovimientoDao movimientoDao, UsuarioDao usuarioDao) {
        this.movimientoDao = movimientoDao;
        this.usuarioDao = usuarioDao;
    }

    @Override
    public void crearMovimiento(MovimientoPeticion request, String emailUsuario) {
        Usuario usuario = usuarioDao.findByEmail(emailUsuario).orElseThrow(() ->
            new RuntimeException("Usuario no encontrado"));
        Movimiento movimiento = new Movimiento();
        movimiento.setUsuarioId(usuario.getId());
        movimiento.setCategoriaId(request.categoriaId());
        movimiento.setMonto(request.monto());
        movimiento.setDescripcion(request.descripcion());
        movimiento.setTipo(request.tipo());

        // Si no mandan fecha, usamos la de hoy
        movimiento.setFecha(request.fecha() != null ? request.fecha() : LocalDate.now());

        movimientoDao.saveMovimiento(movimiento);
    }

    @Override
    public List<Movimiento> listarMisMovimientos(String emailUsuario) {
        Usuario usuario = usuarioDao.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return movimientoDao.findAllByUsuarioId(usuario.getId());
    }
}
