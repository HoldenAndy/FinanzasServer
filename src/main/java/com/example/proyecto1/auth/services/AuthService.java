package com.example.proyecto1.auth.services;

import com.example.proyecto1.auth.dtos.*;

public interface AuthService {
    void registrar(RegisterPeticion request);
    public AuthResponse login (LoginPeticion request);
    public boolean activarCuenta(String codigo);
    UsuarioResponse obtenerPerfil(String email);
    UsuarioResponse actualizarPerfil(String email, ActualizarUsuarioPeticion request);
}
