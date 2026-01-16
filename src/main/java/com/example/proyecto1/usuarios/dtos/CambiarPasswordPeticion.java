package com.example.proyecto1.usuarios.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CambiarPasswordPeticion(
        @NotBlank(message = "Debes ingresar la contraseña actual")
        String passwordActual,
        @NotBlank(message = "La nueva contraseña es obligatoria")
        @Size(min = 6, message = "La nueva contraseña debe tener al menos 6 caracteres")
        String nuevaPassword
) {
}