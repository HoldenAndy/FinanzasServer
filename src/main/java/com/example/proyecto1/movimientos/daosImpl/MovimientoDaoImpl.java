package com.example.proyecto1.movimientos.daosImpl;

import com.example.proyecto1.movimientos.dtos.PaginacionMovimientos;
import com.example.proyecto1.movimientos.entities.Moneda;
import com.example.proyecto1.movimientos.entities.Movimiento;
import com.example.proyecto1.movimientos.entities.TipoMovimiento;
import com.example.proyecto1.movimientos.daos.MovimientoDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public PaginacionMovimientos<Movimiento> findAllByUsuarioId(Long usuarioId, LocalDate fechaInicio, LocalDate fechaFin, int pagina, int tamanio) {
        StringBuilder sqlBase = new StringBuilder(" FROM movimientos m INNER JOIN categorias c ON m.categoria_id = c.id WHERE m.usuario_id = ? ");
        List<Object> params = new ArrayList<>();
        params.add(usuarioId);

        if (fechaInicio != null) {
            sqlBase.append(" AND m.fecha >= ? ");
            params.add(fechaInicio);
        }
        if (fechaFin != null) {
            sqlBase.append(" AND m.fecha <= ? ");
            params.add(fechaFin);
        }

        String sqlCount = "SELECT count(*) " + sqlBase.toString();
        Long totalElementos = jdbcTemplate.queryForObject(sqlCount, Long.class, params.toArray());
        sqlBase.append(" ORDER BY m.fecha DESC LIMIT ? OFFSET ?");
        params.add(tamanio);
        params.add(pagina * tamanio);
        String sqlData = "SELECT m.*, c.nombre as nombre_categoria " + sqlBase.toString();

        List<Movimiento> movimientos = jdbcTemplate.query(
                sqlData,
                movimientoRowMapper,
                params.toArray()
        );
        int totalPaginas = (int) Math.ceil((double) totalElementos / tamanio);
        return new PaginacionMovimientos<>(
                movimientos,
                pagina,
                tamanio,
                totalElementos,
                totalPaginas
        );
    }

    @Override
    public BigDecimal calcularSaldoTotal(Long usuarioId) {
        String sql = "SELECT fun_calcular_saldo(?)";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, usuarioId);
    }

    @Override
    public Optional<Movimiento> findById(Long id) {
        String sql = "SELECT m.*, c.nombre as nombre_categoria " +
                "FROM movimientos m " +
                "INNER JOIN categorias c ON m.categoria_id = c.id " +
                "WHERE m.id = ?";
        try {
            Movimiento mov = jdbcTemplate.queryForObject(sql, movimientoRowMapper, id);
            return Optional.ofNullable(mov);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void actualizarMovimiento(Movimiento mov) {
        String sql = "UPDATE movimientos SET categoria_id = ?, monto = ?, moneda = ?, " +
                "monto_base = ?, descripcion = ?, tipo = ?, fecha = ? WHERE id = ?";

        jdbcTemplate.update(sql,
                mov.getCategoriaId(),
                mov.getMonto(),
                mov.getMoneda().name(),
                mov.getMontoBase(),
                mov.getDescripcion(),
                mov.getTipo().name(),
                mov.getFecha(),
                mov.getId()
        );
    }

    @Override
    public void eliminarMovimiento(Long id) {
        String sql = "DELETE FROM movimientos WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }


}
