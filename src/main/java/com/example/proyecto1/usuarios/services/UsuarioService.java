package com.example.proyecto1.usuarios.services;

import com.example.proyecto1.auth.dtos.ActualizarUsuarioPeticion;
import com.example.proyecto1.auth.dtos.UsuarioResponse;
import org.springframework.stereotype.Service;

public interface UsuarioService {
    UsuarioResponse obtenerUsuario(String email);
    UsuarioResponse actualizarPerfil(String email, ActualizarUsuarioPeticion peticion);
    void cambiarPassword(String email, String passwordActual, String nuevaPassword);
    void solicitarRecuperacion(String email);
    void restablecerPassword(String token, String nuevaPassword);
}
