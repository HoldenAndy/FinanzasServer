package com.example.proyecto1.presupuestos.servicesImpl;

import com.example.proyecto1.currencies.services.CurrencyService;
import com.example.proyecto1.movimientos.daos.MovimientoDao;
import com.example.proyecto1.movimientos.entities.Moneda;
import com.example.proyecto1.presupuestos.daos.PresupuestoDao;
import com.example.proyecto1.presupuestos.dtos.PresupuestoPeticion;
import com.example.proyecto1.presupuestos.dtos.PresupuestoResumen;
import com.example.proyecto1.presupuestos.entities.EstadoPresupuesto;
import com.example.proyecto1.presupuestos.entities.Presupuesto;
import com.example.proyecto1.presupuestos.services.PresupuestoService;
import com.example.proyecto1.usuarios.daos.UsuarioDao;
import com.example.proyecto1.usuarios.entities.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PresupuestoServiceImpl implements PresupuestoService {

    @Value("${app.moneda.base}")
    private String monedaBase;

    private final PresupuestoDao presupuestoDao;
    private final UsuarioDao usuarioDao;
    private final MovimientoDao movimientoDao;
    private final CurrencyService currencyService;

    public PresupuestoServiceImpl(PresupuestoDao presupuestoDao, UsuarioDao usuarioDao, MovimientoDao movimientoDao, CurrencyService currencyService) {
        this.presupuestoDao = presupuestoDao;
        this.usuarioDao = usuarioDao;
        this.movimientoDao = movimientoDao;
        this.currencyService = currencyService;
    }

    @Override
    @Transactional
    public void crearPresupuesto(PresupuestoPeticion peticion, String emailUsuario) {
        Usuario usuario = usuarioDao.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (peticion.fechaFin().isBefore(peticion.fechaInicio())) {
            throw new RuntimeException("La fecha de fin no puede ser anterior a la de inicio");
        }

        boolean existe = presupuestoDao.existePresupuestoEnFechas(usuario.getId(), peticion.categoriaId(), peticion.fechaInicio(), peticion.fechaFin());
        if (existe) {
            throw new RuntimeException("Ya existe un presupuesto para esta categoría en las fechas seleccionadas.");
        }

        Presupuesto presupuesto = new Presupuesto();
        presupuesto.setUsuarioId(usuario.getId());
        presupuesto.setCategoriaId(peticion.categoriaId());
        presupuesto.setMontoLimite(peticion.montoLimite());
        presupuesto.setFechaInicio(peticion.fechaInicio());
        presupuesto.setFechaFin(peticion.fechaFin());
        presupuesto.setMoneda(peticion.moneda());

        presupuestoDao.crearPresupuesto(presupuesto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PresupuestoResumen> obtenerPresupuestosConEstado(String emailUsuario) {
        Usuario usuario = usuarioDao.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Presupuesto> presupuestos = presupuestoDao.listarPorUsuario(usuario.getId());

        return presupuestos.stream().map(p -> {
            BigDecimal gastoRealBase = movimientoDao.calcularGastoPorCategoriaYFechas(
                    usuario.getId(),
                    p.getCategoriaId(),
                    p.getFechaInicio(),
                    p.getFechaFin()
            );


            BigDecimal gastoConvertido = currencyService.convertir(
                    gastoRealBase,
                    Moneda.valueOf(monedaBase),
                    p.getMoneda().name()
            );

            BigDecimal porcentaje = BigDecimal.ZERO;
            if (p.getMontoLimite().compareTo(BigDecimal.ZERO) > 0) {
                porcentaje = gastoConvertido.divide(p.getMontoLimite(), 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(100));
            }

            EstadoPresupuesto estado = EstadoPresupuesto.OK;
            if (porcentaje.doubleValue() >= 100) {
                estado = EstadoPresupuesto.EXCEDIDO;
            } else if (porcentaje.doubleValue() >= 80) {
                estado = EstadoPresupuesto.ALERTA;
            }

            return new PresupuestoResumen(
                    p.getId(),
                    p.getNombreCategoria(),
                    p.getMontoLimite(),
                    gastoConvertido.setScale(2, RoundingMode.HALF_UP),
                    p.getMoneda().name(),
                    p.getFechaInicio(),
                    p.getFechaFin(),
                    porcentaje.setScale(1, RoundingMode.HALF_UP),
                    estado
            );
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminarPresupuesto(Long id, String emailUsuario) {
        Usuario usuario = usuarioDao.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        presupuestoDao.buscarPorIdAndUsuario(id, usuario.getId())
                .orElseThrow(() -> new RuntimeException("Presupuesto no encontrado o no tienes permiso"));

        presupuestoDao.eliminarPresupuesto(id);
    }
}