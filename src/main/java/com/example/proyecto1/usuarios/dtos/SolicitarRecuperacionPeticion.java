package com.example.proyecto1.usuarios.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SolicitarRecuperacionPeticion(
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Por favor, ingrese un email válido")
        String email
) {
}