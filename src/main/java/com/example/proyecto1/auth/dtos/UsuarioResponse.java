package com.example.proyecto1.auth.dtos;

public record UsuarioResponse(
        Long id,
        String nombre,
        String email,
        String role
) {
}
