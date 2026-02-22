package com.example.proyecto1.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginPeticion (
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Ingresa un formato de email valido")
        String email,
        @NotBlank(message = "La contraseña es obligatoria")
        String password
){}
