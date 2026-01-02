package com.example.proyecto1.daos;

import com.example.proyecto1.models.entities.Usuario;

import java.util.Optional;

public interface UsuarioDao {
    Long insertarUsuario(Usuario usuario);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> buscarPorCodigoVerificacion(String codigo);
    void ActivarUsuario(Long id);
}
