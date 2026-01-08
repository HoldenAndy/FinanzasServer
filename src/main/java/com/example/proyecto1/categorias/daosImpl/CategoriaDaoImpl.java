package com.example.proyecto1.categorias.daosImpl;

import com.example.proyecto1.categorias.daos.CategoriaDao;
import com.example.proyecto1.categorias.entities.Categoria;
import com.example.proyecto1.movimientos.entities.TipoMovimiento;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoriaDaoImpl implements CategoriaDao {

    private final JdbcTemplate jdbcTemplate;

    public CategoriaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Categoria> rowMapper = (rs, rowNum) -> {
        Categoria c = new Categoria();
        c.setId(rs.getLong("id"));
        c.setNombre(rs.getString("nombre"));
        c.setTipo(TipoMovimiento.valueOf(rs.getString("tipo"))); // De String DB a Enum Java
        c.setUsuarioId(rs.getLong("usuario_id"));
        return c;
    };

    @Override
    public void insertarCategoriasPorDefecto(Long usuarioId) {
        List<Categoria> categoriasIniciales = List.of(
                new Categoria(null, "Sueldo/Salario", TipoMovimiento.INGRESO, usuarioId),
                new Categoria(null, "Ventas", TipoMovimiento.INGRESO, usuarioId),
                new Categoria(null, "Otros Ingresos", TipoMovimiento.INGRESO, usuarioId),
                new Categoria(null, "Alimentación", TipoMovimiento.EGRESO, usuarioId),
                new Categoria(null, "Transporte", TipoMovimiento.EGRESO, usuarioId),
                new Categoria(null, "Vivienda", TipoMovimiento.EGRESO, usuarioId),
                new Categoria(null, "Servicios", TipoMovimiento.EGRESO, usuarioId),
                new Categoria(null, "Ocio", TipoMovimiento.EGRESO, usuarioId),
                new Categoria(null, "Salud", TipoMovimiento.EGRESO, usuarioId),
                new Categoria(null, "Educación", TipoMovimiento.EGRESO, usuarioId)
        );

        String sql = "INSERT INTO categorias (nombre, tipo, usuario_id) VALUES (?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, categoriasIniciales, categoriasIniciales.size(),
                (ps, categoria) -> {
                    ps.setString(1, categoria.getNombre());
                    ps.setString(2, categoria.getTipo().name());
                    ps.setLong(3, usuarioId);
                }
        );
    }

    @Override
    public List<Categoria> findAllByUsuarioId(Long usuarioId) {
        String sql = "SELECT * FROM categorias WHERE usuario_id = ?";
        return jdbcTemplate.query(sql, rowMapper, usuarioId);
    }

    @Override
    public void crearCategoria(Categoria categoria) {
        String sql = "INSERT INTO categorias (nombre, tipo, usuario_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                categoria.getNombre(),
                categoria.getTipo().name(),
                categoria.getUsuarioId()
        );
    }

    @Override
    public void actualizarCategoria(Categoria categoria) {
        String sql = "UPDATE categorias SET nombre = ?, tipo = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                categoria.getNombre(),
                categoria.getTipo().name(),
                categoria.getId()
        );
    }

    @Override
    public void eliminarCategoria(Long id) {
        String sql = "DELETE FROM categorias WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<Categoria> findById(Long id) {
        String sql = "SELECT * FROM categorias WHERE id = ?";
        try {
            Categoria cat = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(cat);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}