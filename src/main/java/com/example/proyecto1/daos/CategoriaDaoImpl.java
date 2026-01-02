package com.example.proyecto1.daos;

import com.example.proyecto1.models.entities.Categoria;
import com.example.proyecto1.models.entities.TipoMovimiento;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public class CategoriaDaoImpl implements CategoriaDao {
    private final JdbcTemplate jdbcTemplate;

    public CategoriaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Categoria> rowMapper = (rs, rowNum) -> {
        Categoria cat = new Categoria();
        cat.setId(rs.getLong("id"));
        cat.setNombre(rs.getString("nombre"));
        cat.setTipo(TipoMovimiento.valueOf(rs.getString("tipo")));
        cat.setUsuarioId(rs.getLong("usuario_id"));
        return cat;
    };

    @Override
    public void insertarCategoriasPorDefecto(Long usuarioId) {
        String sql = "INSERT INTO categorias (nombre, tipo, usuario_id) VALUES (?, ?, ?)";
        List<Object[]> categorias = Arrays.asList(
                // INGRESOS
                new Object[]{"Sueldo/Salario", TipoMovimiento.INGRESO.name(), usuarioId},
                new Object[]{"Ventas", TipoMovimiento.INGRESO.name(), usuarioId},
                new Object[]{"Inversiones", TipoMovimiento.INGRESO.name(), usuarioId},
                // EGRESOS
                new Object[]{"Alimentación", TipoMovimiento.EGRESO.name(), usuarioId},
                new Object[]{"Vivienda", TipoMovimiento.EGRESO.name(), usuarioId},
                new Object[]{"Transporte", TipoMovimiento.EGRESO.name(), usuarioId},
                new Object[]{"Servicios", TipoMovimiento.EGRESO.name(), usuarioId},
                new Object[]{"Salud", TipoMovimiento.EGRESO.name(), usuarioId},
                new Object[]{"Entretenimiento", TipoMovimiento.EGRESO.name(), usuarioId},
                new Object[]{"Compras", TipoMovimiento.EGRESO.name(), usuarioId}
        );

        jdbcTemplate.batchUpdate(sql, categorias);
    }

    @Override
    public List<Categoria> findAllByUsuarioId(Long usuarioId) {
        String sql = "SELECT * FROM categorias WHERE usuario_id = ?";
        return jdbcTemplate.query(sql, rowMapper, usuarioId);
    }
}
