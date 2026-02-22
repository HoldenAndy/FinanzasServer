package com.example.proyecto1.auth.controllers;

import com.example.proyecto1.auth.dtos.*;
import com.example.proyecto1.auth.services.AuthService;
import com.example.proyecto1.usuarios.dtos.RestablecerPasswordPeticion;
import com.example.proyecto1.usuarios.dtos.SolicitarRecuperacionPeticion;
import com.example.proyecto1.usuarios.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authServices;
    private final UsuarioService usuarioService;

    public AuthController(AuthService authServices, UsuarioService usuarioService) {
        this.authServices = authServices;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registrarUsuario(@Valid @RequestBody RegisterPeticion request){ // Cambia String por Map
        try {
            authServices.registrar(request);
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Registro de usuario éxitoso!");
            return ResponseEntity.ok(respuesta);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loguearUsuario(@Valid @RequestBody LoginPeticion loginPeticion){
        return ResponseEntity.ok(authServices.login(loginPeticion));
    }

    @GetMapping(value = "/confirmar", produces = "text/html;charset=UTF-8")
    public  ResponseEntity<String> activarCuenta (@RequestParam("codigo") String codigo){
        boolean activado = authServices.activarCuenta(codigo);

        if(activado){
            return ResponseEntity.ok("<h1>¡Cuenta activada!</h1><p>Ya puedes iniciar sesión en la app.</p>");
        }else {
            return ResponseEntity.badRequest().body("<h1>Error</h1><p>El código de activación es inválido o ya fue usado.</p>");
        }
    }

    @PostMapping("/solicitar-recuperacion")
    public ResponseEntity<Map<String,String>> solicitarRecuperacion(@Valid @RequestBody SolicitarRecuperacionPeticion peticion) {
        usuarioService.solicitarRecuperacion(peticion.email());
        return ResponseEntity.ok((Map.of("mensaje","Te hemos enviado instrucciones al correo, por favor revisalo.")));
    }

    @PostMapping("/restablecer-password")
    public ResponseEntity<Map<String, String>> restablecerPassword(@Valid @RequestBody RestablecerPasswordPeticion peticion
    ) {
        usuarioService.restablecerPassword(peticion.token(), peticion.nuevaPassword());
        return ResponseEntity.ok(Map.of("mensaje", "Contraseña restablecida con éxito. Ya puedes iniciar sesión."));
    }
}
