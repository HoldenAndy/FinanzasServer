package com.example.proyecto1.aiApi.servicesImpl;

import com.example.proyecto1.aiApi.services.FinancialContextService;
import com.example.proyecto1.movimientos.daos.MovimientoDao;
import com.example.proyecto1.movimientos.entities.TipoMovimiento;
import com.example.proyecto1.presupuestos.daos.PresupuestoDao;
import com.example.proyecto1.presupuestos.entities.Presupuesto;
import com.example.proyecto1.reportes.dtos.ReporteBalanceDTO;
import com.example.proyecto1.reportes.dtos.ReporteCategoriaDTO;
import com.example.proyecto1.reportes.dtos.ReporteMensualDTO;
import com.example.proyecto1.usuarios.daos.UsuarioDao;
import com.example.proyecto1.usuarios.entities.Usuario;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinancialContextServiceImpl implements FinancialContextService {

    private final MovimientoDao movimientoDao;
    private final UsuarioDao usuarioDao;
    private final PresupuestoDao presupuestoDao;

    public FinancialContextServiceImpl(MovimientoDao movimientoDao, UsuarioDao usuarioDao, PresupuestoDao presupuestoDao) {
        this.movimientoDao = movimientoDao;
        this.usuarioDao = usuarioDao;
        this.presupuestoDao = presupuestoDao;
    }

    @Override
    public String obtenerContextoFinanciero(String email) {
        Usuario usuario = usuarioDao.findByEmail(email).orElseThrow();
        Long uid = usuario.getId();
        LocalDate hoy = LocalDate.now();
        LocalDate inicioMes = hoy.withDayOfMonth(1);
        LocalDate finMes = hoy.withDayOfMonth(hoy.lengthOfMonth());

        ReporteBalanceDTO balance = movimientoDao.obtenerBalanceGeneral(uid, inicioMes, finMes);

        List<ReporteCategoriaDTO> categorias = movimientoDao.obtenerDistribucionPorCategoria(uid, inicioMes, finMes, TipoMovimiento.EGRESO);
        String topGastos = categorias.stream()
                .limit(3)
                .map(c -> String.format("- %s: %s (%s%%)", c.categoria(), c.totalGastado(), c.porcentajeDelTotal()))
                .collect(Collectors.joining("\n"));

        List<ReporteMensualDTO> historia = movimientoDao.obtenerHistorialUltimos6Meses(uid);
        String tendencia = historia.stream()
                .map(h -> String.format("[%s: +%s / -%s]", h.etiqueta(), h.ingresos(), h.egresos()))
                .collect(Collectors.joining("; "));

        List<ReporteMensualDTO> diario = movimientoDao.obtenerGastosDiarios(uid, hoy.getYear(), hoy.getMonthValue());
        String comportamientoDiario = diario.stream()
                .filter(d -> d.egresos().compareTo(BigDecimal.ZERO) > 0) // Solo días con gastos
                .map(d -> String.format("Día %s: -%s", d.etiqueta(), d.egresos()))
                .collect(Collectors.joining(", "));

        List<Presupuesto> presupuestos = presupuestoDao.listarPorUsuario(uid);
        String estadoPresupuestos = presupuestos.stream().map(p -> {
            BigDecimal gastado = movimientoDao.calcularGastoPorCategoriaYFechas(uid, p.getCategoriaId(), inicioMes, finMes);
            BigDecimal porcentaje = BigDecimal.ZERO;
            if (p.getMontoLimite().compareTo(BigDecimal.ZERO) > 0) {
                porcentaje = gastado.divide(p.getMontoLimite(), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
            }
            return String.format("- %s: Límite %s, Gastado %s (%s%%)",
                    p.getNombreCategoria(), p.getMontoLimite(), gastado, porcentaje);
        }).collect(Collectors.joining("\n"));

        if (estadoPresupuestos.isEmpty()) estadoPresupuestos = "No hay presupuestos definidos.";

        return String.format("""
            ACTÚA COMO UN ASESOR FINANCIERO EXPERTO. ANALIZA ESTOS DATOS:
            
            1. BALANCE MES ACTUAL:
            - Ingresos: %s | Gastos: %s | Ahorro: %s (%s%%)
            
            2. EN QUÉ SE FUE EL DINERO (Top 3):
            %s
            
            3. TENDENCIA (Últimos 6 meses):
            %s
            
            4. COMPORTAMIENTO DIARIO (Días de gasto):
            %s
            
            5. CUMPLIMIENTO DE PRESUPUESTOS (Límites):
            %s
            
            Basado en esto, dame 3 consejos cortos, directos y con emojis. 
            Si ves que se pasó en algún presupuesto, regáñalo.
            """,
                balance.totalIngresos(), balance.totalEgresos(), balance.balanceTotal(), balance.ahorroPorcentaje(),
                topGastos,
                tendencia,
                comportamientoDiario,
                estadoPresupuestos
        );
    }
}