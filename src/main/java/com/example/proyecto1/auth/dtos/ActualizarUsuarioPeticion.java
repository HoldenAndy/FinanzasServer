package com.example.proyecto1.auth.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ActualizarUsuarioPeticion(
        @NotBlank(message = "El nombre no puede estar vacío")
        String nombre,
        @Size(min = 6, message = "La nueva contraseña debe tener al menos 6 caracteres")
        String password
) {
}
