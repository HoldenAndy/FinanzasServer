package com.example.proyecto1.usuarios.daos;

import com.example.proyecto1.usuarios.entities.Usuario;

import java.util.Optional;

public interface UsuarioDao {
    Long insertarUsuario(Usuario usuario);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> buscarPorCodigoVerificacion(String codigo);
    void ActivarUsuario(Long id);
    void actualizarUsuario(Usuario usuario);
    void actualizarPassword(Long usuarioId, String nuevaPasswordEncriptada);
    void guardarTokenRecuperacion(String email, String token, java.time.LocalDateTime expiracion);
    java.util.Optional<com.example.proyecto1.usuarios.entities.Usuario> findByTokenRecuperacion(String token);
}
