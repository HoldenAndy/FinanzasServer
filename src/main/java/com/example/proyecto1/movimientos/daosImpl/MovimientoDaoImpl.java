package com.example.proyecto1.movimientos.daosImpl;

import com.example.proyecto1.movimientos.entities.Moneda;
import com.example.proyecto1.movimientos.entities.Movimiento;
import com.example.proyecto1.movimientos.entities.TipoMovimiento;
import com.example.proyecto1.movimientos.daos.MovimientoDao;
import com.example.proyecto1.reportes.dtos.ReporteCategoriaDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public class MovimientoDaoImpl implements MovimientoDao {

    private final JdbcTemplate jdbcTemplate;

    public MovimientoDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Movimiento> movimientoRowMapper = (rs, rowNum) -> {
        Movimiento mov = new Movimiento();
        mov.setId(rs.getLong("id"));
        mov.setUsuarioId(rs.getLong("usuario_id"));
        mov.setCategoriaId(rs.getLong("categoria_id"));
        mov.setNombreCategoria(rs.getString("nombre_categoria"));
        mov.setMonto(rs.getBigDecimal("monto"));
        mov.setMoneda(Moneda.valueOf(rs.getString("moneda")));
        mov.setMontoBase(rs.getBigDecimal("monto_base"));
        mov.setDescripcion(rs.getString("descripcion"));
        mov.setTipo(TipoMovimiento.valueOf(rs.getString("tipo")));
        mov.setFecha(rs.getDate("fecha").toLocalDate());
        return mov;
    };

    @Override
    public void saveMovimiento(Movimiento mov) {
        String sql = "INSERT INTO movimientos (usuario_id, categoria_id, monto, moneda, monto_base, descripcion, tipo, fecha) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                mov.getUsuarioId(),
                mov.getCategoriaId(),
                mov.getMonto(),
                mov.getMoneda().name(),
                mov.getMontoBase(),
                mov.getDescripcion(),
                mov.getTipo().name(),
                mov.getFecha()
        );
    }

    @Override
    public List<Movimiento> findAllByUsuarioId(Long usuarioId) {
        String sql = "SELECT m.*, c.nombre as nombre_categoria " +
                "FROM movimientos m " +
                "INNER JOIN categorias c ON m.categoria_id = c.id " +
                "WHERE m.usuario_id = ? " +
                "ORDER BY m.fecha DESC";
        return jdbcTemplate.query(sql, movimientoRowMapper, usuarioId);
    }

    @Override
    public List<ReporteCategoriaDTO> obtenerDistribucionPorCategoria(Long usuarioId, LocalDate inicio, LocalDate fin, TipoMovimiento tipo) {
        String sqlTotal = "SELECT COALESCE(SUM(monto_base), 0) FROM movimientos " +
                "WHERE usuario_id = ? AND tipo = ? AND fecha BETWEEN ? AND ?"; // <--- Aquí cambiamos 'EGRESO' por ?

        BigDecimal totalGlobal = jdbcTemplate.queryForObject(sqlTotal, BigDecimal.class, usuarioId, tipo.name(), inicio, fin);

        if (totalGlobal == null || totalGlobal.compareTo(BigDecimal.ZERO) == 0) {
            return List.of();
        }

        String sql = "SELECT c.nombre, SUM(m.monto_base) as total " +
                "FROM movimientos m " +
                "INNER JOIN categorias c ON m.categoria_id = c.id " +
                "WHERE m.usuario_id = ? AND m.tipo = ? AND m.fecha BETWEEN ? AND ? " + // <--- Aquí también
                "GROUP BY c.nombre " +
                "ORDER BY total DESC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            String categoria = rs.getString("nombre");
            BigDecimal totalCategoria = rs.getBigDecimal("total");

            BigDecimal porcentaje = totalCategoria.divide(totalGlobal, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(100));

            return new ReporteCategoriaDTO(
                    categoria,
                    totalCategoria,
                    porcentaje.setScale(2, java.math.RoundingMode.HALF_UP)
            );
        }, usuarioId, tipo.name(), inicio, fin);
    }

    @Override
    public BigDecimal calcularSaldoTotal(Long usuarioId) {
        String sql = "SELECT " +
                "COALESCE(SUM(CASE WHEN tipo = 'INGRESO' THEN monto_base ELSE 0 END), 0) - " +
                "COALESCE(SUM(CASE WHEN tipo = 'EGRESO' THEN monto_base ELSE 0 END), 0) " +
                "FROM movimientos WHERE usuario_id = ?";

        return jdbcTemplate.queryForObject(sql, BigDecimal.class, usuarioId);
    }

    @Override
    public BigDecimal calcularGastoPorCategoriaYFechas(Long usuarioId, Long categoriaId, LocalDate inicio, LocalDate fin) {
        String sql = "SELECT COALESCE(SUM(monto_base), 0) " +
                "FROM movimientos " +
                "WHERE usuario_id = ? " +
                "AND categoria_id = ? " +
                "AND tipo = 'EGRESO' " +
                "AND fecha BETWEEN ? AND ?";

        return jdbcTemplate.queryForObject(sql, BigDecimal.class, usuarioId, categoriaId, inicio, fin);
    }

}
