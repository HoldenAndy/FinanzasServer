package com.example.proyecto1.usuarios.controllers;

import com.example.proyecto1.auth.dtos.ActualizarUsuarioPeticion;
import com.example.proyecto1.auth.dtos.UsuarioResponse;
import com.example.proyecto1.auth.services.AuthService;
import com.example.proyecto1.usuarios.dtos.CambiarPasswordPeticion;
import com.example.proyecto1.usuarios.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthService authServices;

    public UsuarioController(UsuarioService usuarioService, AuthService authServices) {
        this.usuarioService = usuarioService;
        this.authServices = authServices;
    }

    @PutMapping("/cambiar-password")
    public ResponseEntity<Map<String, String>> cambiarPassword(
            @Valid @RequestBody CambiarPasswordPeticion peticion,
            Principal principal) {

        usuarioService.cambiarPassword(
                principal.getName(),
                peticion.passwordActual(),
                peticion.nuevaPassword()
        );

        return ResponseEntity.ok(Map.of("mensaje","Contraseña actualizada correctamente"));
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> obtenerPerfil(java.security.Principal principal) {
        return ResponseEntity.ok(authServices.obtenerPerfil(principal.getName()));
    }

    @PutMapping("/me")
    public ResponseEntity<UsuarioResponse> actualizarPerfil(@Valid @RequestBody ActualizarUsuarioPeticion request, java.security.Principal principal) {
        return ResponseEntity.ok(authServices.actualizarPerfil(principal.getName(), request));
    }
}
