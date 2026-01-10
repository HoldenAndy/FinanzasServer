package com.example.proyecto1.usuarios.dtos;

public record CambiarPasswordPeticion(
        String passwordActual,
        String nuevaPassword
) {}