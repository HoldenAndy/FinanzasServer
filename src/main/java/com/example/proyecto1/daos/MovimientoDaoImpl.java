package com.example.proyecto1.daos;

import com.example.proyecto1.models.entities.Movimiento;
import com.example.proyecto1.models.entities.TipoMovimiento;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class MovimientoDaoImpl implements MovimientoDao{

    private final JdbcTemplate jdbcTemplate;

    public MovimientoDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public final RowMapper<Movimiento> movimientoRowMapper = (rs, rowNum) -> {
        Movimiento movimiento = new Movimiento();
        movimiento.setId(rs.getLong("id"));
        movimiento.setUsuarioId(rs.getLong("usuario_id"));
        movimiento.setCategoriaId(rs.getLong("categoria_id"));
        movimiento.setMonto(rs.getBigDecimal("monto"));
        movimiento.setFecha(rs.getDate("fecha").toLocalDate());
        movimiento.setDescripcion(rs.getString("descripcion"));
        movimiento.setTipo(TipoMovimiento.valueOf(rs.getString("tipo")));
        return movimiento;
    };

    @Override
    public void saveMovimiento(Movimiento movimiento) {
        String sql = "INSERT INTO movimientos (usuario_id, categoria_id, monto, fecha, descripcion, tipo) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                movimiento.getUsuarioId(),
                movimiento.getCategoriaId(),
                movimiento.getMonto(),
                movimiento.getFecha(),
                movimiento.getDescripcion(),
                movimiento.getTipo().name()
        );
    }

    @Override
    public List<Movimiento> findAllByUsuarioId(Long usuarioId) {
        String sql = "SELECT * FROM movimientos WHERE usuario_id = ? ORDER BY fecha DESC";
        return jdbcTemplate.query(sql, movimientoRowMapper, usuarioId);
    }

    @Override
    public BigDecimal calcularSaldoTotal(Long usuarioId) {
        String sql = "SELECT " +
                "SUM(CASE WHEN tipo = 'INGRESO' THEN monto ELSE 0 END) - " +
                "SUM(CASE WHEN tipo = 'EGRESO' THEN monto ELSE 0 END) " +
                "FROM movimientos WHERE usuario_id = ?";

        BigDecimal saldo = jdbcTemplate.queryForObject(sql, BigDecimal.class, usuarioId);
        return saldo != null ? saldo : BigDecimal.ZERO;
    }
}
