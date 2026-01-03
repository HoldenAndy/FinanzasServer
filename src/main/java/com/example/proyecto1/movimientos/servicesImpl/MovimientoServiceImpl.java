package com.example.proyecto1.movimientos.servicesImpl;

import com.example.proyecto1.currencies.services.CurrencyService;
import com.example.proyecto1.movimientos.daos.MovimientoDao;
import com.example.proyecto1.movimientos.dtos.PaginacionMovimientos;
import com.example.proyecto1.movimientos.services.MovimientoService;
import com.example.proyecto1.usuarios.daos.UsuarioDao;
import com.example.proyecto1.movimientos.dtos.MovimientoPeticion;
import com.example.proyecto1.movimientos.entities.Movimiento;
import com.example.proyecto1.usuarios.entities.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class MovimientoServiceImpl implements MovimientoService {

    @Value("${app.moneda.base}")
    private String monedaBase;

    private final MovimientoDao movimientoDao;
    private final UsuarioDao usuarioDao;
    private final CurrencyService currencyService;

    public MovimientoServiceImpl(MovimientoDao movimientoDao, UsuarioDao usuarioDao, CurrencyService currencyService) {
        this.movimientoDao = movimientoDao;
        this.usuarioDao = usuarioDao;
        this.currencyService = currencyService;
    }

    @Override
    @Transactional
    public void crearMovimiento(MovimientoPeticion request, String emailUsuario) {
        Usuario usuario = usuarioDao.findByEmail(emailUsuario).orElseThrow(() ->
            new RuntimeException("Usuario no encontrado"));
        BigDecimal montoConvertido = currencyService.convertir(request.monto(), request.moneda(), monedaBase);
        Movimiento movimiento = new Movimiento();
        movimiento.setUsuarioId(usuario.getId());
        movimiento.setCategoriaId(request.categoriaId());
        movimiento.setMonto(request.monto());
        movimiento.setMoneda(request.moneda());
        movimiento.setMontoBase(montoConvertido);
        movimiento.setDescripcion(request.descripcion());
        movimiento.setTipo(request.tipo());
        movimiento.setFecha(request.fecha() != null ? request.fecha() : LocalDate.now());
        movimientoDao.saveMovimiento(movimiento);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginacionMovimientos<Movimiento> listarMisMovimientos(String emailUsuario, LocalDate fechaInicio, LocalDate fechaFin, int pagina, int tamanio) {
        Usuario usuario = usuarioDao.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return movimientoDao.findAllByUsuarioId(usuario.getId(), fechaInicio, fechaFin,
                pagina, tamanio);
    }

    @Override
    @Transactional(readOnly=true)
    public Map<String, Object> obtenerResumenFinanciero(String email) {
        Usuario usuario = usuarioDao.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        BigDecimal saldoTotal = movimientoDao.calcularSaldoTotal(usuario.getId());

        Map<String, Object> resumen = new HashMap<>();
        resumen.put("saldoTotal", saldoTotal);
        resumen.put("usuario", usuario.getNombre());

        return resumen;
    }

    @Override
    @Transactional
    public void editarMovimiento(Long id, MovimientoPeticion peticion, String emailUsuario) {
        Usuario usuario = usuarioDao.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Movimiento movimiento = movimientoDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));

        if (!movimiento.getUsuarioId().equals(usuario.getId())) {
            throw new RuntimeException("¡Acceso Denegado! No puedes editar movimientos de otro usuario.");
        }

        movimiento.setDescripcion(peticion.descripcion());
        movimiento.setFecha(peticion.fecha());
        movimiento.setCategoriaId(peticion.categoriaId());
        movimiento.setTipo(peticion.tipo());

        boolean cambioMonto = movimiento.getMonto().compareTo(peticion.monto()) != 0;
        boolean cambioMoneda = !movimiento.getMoneda().name().equals(peticion.moneda());

        if (cambioMonto || cambioMoneda) {
            movimiento.setMonto(peticion.monto());
            movimiento.setMoneda(peticion.moneda());

            BigDecimal nuevoMontoBase = currencyService.convertir(
                    peticion.monto(),
                    peticion.moneda(),
                    monedaBase
            );
            movimiento.setMontoBase(nuevoMontoBase);
        }

        movimientoDao.actualizarMovimiento(movimiento);
    }

    @Override
    @Transactional
    public void eliminarMovimiento(Long id, String emailUsuario) {
        Usuario usuario = usuarioDao.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Movimiento movimiento = movimientoDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));

        // SECURITY CHECK
        if (!movimiento.getUsuarioId().equals(usuario.getId())) {
            throw new RuntimeException("No puedes eliminar este movimiento.");
        }

        movimientoDao.eliminarMovimiento(id);
    }

}
