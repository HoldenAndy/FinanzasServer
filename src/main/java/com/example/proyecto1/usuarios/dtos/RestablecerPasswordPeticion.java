package com.example.proyecto1.usuarios.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RestablecerPasswordPeticion(
        @NotBlank (message = "El token es obligatorio")
        String token,
        @NotBlank (message = "La nueva contraseña es obligatoria")
        @Size(min = 6, message = "La nueva contraseña debe tener al menos 6 caracteres")
        String nuevaPassword
) {}