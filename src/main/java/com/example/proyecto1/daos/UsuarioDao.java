package com.example.proyecto1.daos;

import com.example.proyecto1.models.entities.Role;
import com.example.proyecto1.models.entities.Usuario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioDao{
    private final JdbcTemplate jdbcTemplate;

    public UsuarioDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Usuario> usuarioRowMapper = (rs, rowNum) -> {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getLong("id"));
        usuario.setEmail(rs.getString("email"));
        usuario.setNombre(rs.getString("email"));
        usuario.setPassword(rs.getString("password"));
        usuario.setRole(Role.valueOf(rs.getString("role")));
        return usuario;
    };

    public void save(Usuario usuario){
        String sql = "INSERT INTO usuarios (email, password, nombre, role) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
            usuario.getEmail(),
            usuario.getPassword(),
            usuario.getNombre(),
            usuario.getRole().name());
    }

}
