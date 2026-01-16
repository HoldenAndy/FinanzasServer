package com.example.proyecto1.presupuestos.daosImpl;

import com.example.proyecto1.movimientos.entities.Moneda;
import com.example.proyecto1.presupuestos.daos.PresupuestoDao;
import com.example.proyecto1.presupuestos.entities.Presupuesto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class PresupuestoDaoImpl implements PresupuestoDao {

    private final JdbcTemplate jdbcTemplate;

    public PresupuestoDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Presupuesto> presupuestoRowMapper = (rs, rowNum) -> {
        Presupuesto p = new Presupuesto();
        p.setId(rs.getLong("id"));
        p.setUsuarioId(rs.getLong("usuario_id"));
        p.setCategoriaId(rs.getLong("categoria_id"));
        p.setNombreCategoria(rs.getString("nombre_categoria"));
        p.setMontoLimite(rs.getBigDecimal("monto_limite"));
        p.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
        p.setFechaFin(rs.getDate("fecha_fin").toLocalDate());
        // Manejo seguro del Enum por si acaso
        String monedaStr = rs.getString("moneda");
        p.setMoneda(monedaStr != null ? Moneda.valueOf(monedaStr) : Moneda.USD);
        return p;
    };

    @Override
    public void crearPresupuesto(Presupuesto p) {
        String sql = "INSERT INTO presupuestos (usuario_id, categoria_id, monto_limite, fecha_inicio, fecha_fin, moneda) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, p.getUsuarioId(), p.getCategoriaId(), p.getMontoLimite(), p.getFechaInicio(), p.getFechaFin(), p.getMoneda().name());
    }

    @Override
    public List<Presupuesto> listarPorUsuario(Long usuarioId) {
        String sql = "SELECT p.*, c.nombre as nombre_categoria " +
                "FROM presupuestos p " +
                "INNER JOIN categorias c ON p.categoria_id = c.id " +
                "WHERE p.usuario_id = ? " +
                "ORDER BY p.fecha_fin DESC";
        return jdbcTemplate.query(sql, presupuestoRowMapper, usuarioId);
    }

    @Override
    public Optional<Presupuesto> buscarPorIdAndUsuario(Long id, Long usuarioId) {
        String sql = "SELECT p.*, c.nombre as nombre_categoria FROM presupuestos p " +
                "INNER JOIN categorias c ON p.categoria_id = c.id " +
                "WHERE p.id = ? AND p.usuario_id = ?";
        try {
            Presupuesto p = jdbcTemplate.queryForObject(sql, presupuestoRowMapper, id, usuarioId);
            return Optional.ofNullable(p);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void eliminarPresupuesto(Long id) {
        String sql = "DELETE FROM presupuestos WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existePresupuestoEnFechas(Long usuarioId, Long categoriaId, LocalDate inicio, LocalDate fin) {
        String sql = "SELECT COUNT(*) FROM presupuestos WHERE usuario_id = ? AND categoria_id = ? " +
                "AND ((fecha_inicio BETWEEN ? AND ?) OR (fecha_fin BETWEEN ? AND ?))";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, usuarioId, categoriaId, inicio, fin, inicio, fin);
        return count != null && count > 0;
    }
}