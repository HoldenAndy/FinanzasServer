package com.example.proyecto1.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterPeticion (
        @NotBlank (message = "El email no puede estar vacio")
        @Email (message = "El email debe tener un formato valido")
        String email,

        @NotBlank(message = "La contraseña no puede estar vacia")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        String password,

        @NotBlank (message = "El nombre es obligatorio")
        String nombre
) {}