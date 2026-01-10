package com.example.proyecto1.usuarios.dtos;

public record RestablecerPasswordPeticion(
        String token,
        String nuevaPassword
) {}