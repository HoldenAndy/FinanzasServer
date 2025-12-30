package com.example.proyecto1.daos;

import com.example.proyecto1.models.entities.Role;
import com.example.proyecto1.models.entities.Usuario;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UsuarioDaoImpl implements UsuarioDao {
    private final JdbcTemplate jdbcTemplate;

    public UsuarioDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Usuario> usuarioRowMapper = (rs, rowNum) -> {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getLong("id"));
        usuario.setEmail(rs.getString("email"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setPassword(rs.getString("password"));
        usuario.setRole(Role.valueOf(rs.getString("role")));
        usuario.setActivado(rs.getBoolean("activado"));
        usuario.setCodigoVerificacion(rs.getString("codigo_verificacion"));
        return usuario;
    };

    @Override
    public void save(Usuario usuario){
        String sql = "INSERT INTO usuarios (email, password, nombre, role, activado, codigo_verificacion) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
            usuario.getEmail(),
            usuario.getPassword(),
            usuario.getNombre(),
            usuario.getRole().name(),
            usuario.isActivado(),
            usuario.getCodigoVerificacion());
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, usuarioRowMapper, email));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Usuario> buscarPorCodigoVerificacion(String codigo){
        String sql = "SELECT * FROM usuarios WHERE codigo_verificacion = ?";
        try {
            Usuario usuario = jdbcTemplate.queryForObject(sql, usuarioRowMapper, codigo);
            return Optional.ofNullable(usuario);
        }catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public void ActivarUsuario(Long id){
        String sql = "UPDATE usuarios SET activado = true, codigo_verificacion = NULL WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
