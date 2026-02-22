package com.example.proyecto1.reportes.servicesImpl;

import com.example.proyecto1.movimientos.daos.MovimientoDao;
import com.example.proyecto1.movimientos.entities.TipoMovimiento;
import com.example.proyecto1.reportes.dtos.ReporteBalanceDTO;
import com.example.proyecto1.reportes.dtos.ReporteCategoriaDTO;
import com.example.proyecto1.reportes.dtos.ReporteMensualDTO;
import com.example.proyecto1.reportes.services.ReporteService;
import com.example.proyecto1.usuarios.daos.UsuarioDao;
import com.example.proyecto1.usuarios.entities.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class ReporteServiceImpl implements ReporteService {
    private final MovimientoDao movimientoDao;
    private final UsuarioDao usuarioDao;

    public ReporteServiceImpl(MovimientoDao movimientoDao, UsuarioDao usuarioDao) {
        this.movimientoDao = movimientoDao;
        this.usuarioDao = usuarioDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteCategoriaDTO> obtenerDistribucion(String email, LocalDate inicio, LocalDate fin, TipoMovimiento tipo) {
        Usuario usuario = usuarioDao.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (inicio == null || fin == null) {
            LocalDate hoy = LocalDate.now();
            inicio = hoy.with(TemporalAdjusters.firstDayOfMonth());
            fin = hoy.with(TemporalAdjusters.lastDayOfMonth());
        }

        return movimientoDao.obtenerDistribucionPorCategoria(usuario.getId(), inicio, fin, tipo);
    }

    @Override
    public ReporteBalanceDTO obtenerBalance(String email, LocalDate inicio, LocalDate fin) {
        Usuario usuario = usuarioDao.findByEmail(email).orElseThrow();
        if (inicio == null || fin == null) {
            LocalDate hoy = LocalDate.now();
            inicio = hoy.with(java.time.temporal.TemporalAdjusters.firstDayOfMonth());
            fin = hoy.with(java.time.temporal.TemporalAdjusters.lastDayOfMonth());
        }
        return movimientoDao.obtenerBalanceGeneral(usuario.getId(), inicio, fin);
    }

    @Override
    public List<ReporteMensualDTO> obtenerHistorial(String email) {
        Usuario usuario = usuarioDao.findByEmail(email).orElseThrow();
        return movimientoDao.obtenerHistorialUltimos6Meses(usuario.getId());
    }

    @Override
    public List<ReporteMensualDTO> obtenerDiario(String email, int anio, int mes) {
        Usuario usuario = usuarioDao.findByEmail(email).orElseThrow();
        return movimientoDao.obtenerGastosDiarios(usuario.getId(), anio, mes);
    }

}
