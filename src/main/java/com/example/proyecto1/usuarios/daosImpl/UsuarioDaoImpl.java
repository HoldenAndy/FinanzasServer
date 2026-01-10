package com.example.proyecto1.usuarios.daosImpl;

import com.example.proyecto1.usuarios.entities.Role;
import com.example.proyecto1.usuarios.entities.Usuario;
import com.example.proyecto1.usuarios.daos.UsuarioDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
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
        usuario.setTokenRecuperacionPassword(rs.getString("token_recuperacion"));
        java.sql.Timestamp fechaExpiracion = rs.getTimestamp("token_expiracion");
        if (fechaExpiracion != null) {
            usuario.setTokenExpiracionPassword(fechaExpiracion.toLocalDateTime());
        }
        return usuario;
    };

    @Override
    public Long insertarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (email, password, nombre, role, activado, codigo_verificacion) VALUES (?, ?, ?, ?, ?, ?)";

        // Objeto para atrapar el ID autoincremental
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, usuario.getEmail());
            ps.setString(2, usuario.getPassword());
            ps.setString(3, usuario.getNombre());
            ps.setString(4, usuario.getRole().name());
            ps.setBoolean(5, usuario.isActivado());
            ps.setString(6, usuario.getCodigoVerificacion());
            return ps;
        }, keyHolder);

        return keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;
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
    @Override
    public void actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                usuario.getNombre(),
                usuario.getPassword(),
                usuario.getId()
        );
    }

    @Override
    public void actualizarPassword(Long usuarioId, String nuevaPasswordEncriptada) {
        String sql = "UPDATE usuarios SET password = ? WHERE id = ?";
        jdbcTemplate.update(sql, nuevaPasswordEncriptada, usuarioId);
    }

    @Override
    public void guardarTokenRecuperacion(String email, String token, LocalDateTime expiracion) {
        String sql = "UPDATE usuarios SET token_recuperacion = ?, token_expiracion = ? WHERE email = ?";
        jdbcTemplate.update(sql, token, expiracion, email);
    }

    @Override
    public Optional<Usuario> findByTokenRecuperacion(String token) {
        String sql = "SELECT * FROM usuarios WHERE token_recuperacion = ?";
        try {
            Usuario u = jdbcTemplate.queryForObject(sql, usuarioRowMapper, token);
            return Optional.ofNullable(u);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
