package com.example.proyecto1.controllers;

import com.example.proyecto1.models.dtos.AuthResponse;
import com.example.proyecto1.models.dtos.LoginPeticion;
import com.example.proyecto1.models.dtos.RegisterPeticion;
import com.example.proyecto1.models.entities.Usuario;
import com.example.proyecto1.services.AuthServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthServices authServices;

    public AuthController(AuthServices authServices) {
        this.authServices = authServices;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registrar(@RequestBody RegisterPeticion request){
        try {
            authServices.registrar(request);
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Registro de usuario éxitoso!");
            return ResponseEntity.ok(respuesta.toString());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginPeticion loginPeticion){
        try {
            AuthResponse token = authServices.login(loginPeticion);
            return ResponseEntity.ok(token);
        }catch (Exception e){
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @GetMapping("/confirmar")
    public  ResponseEntity<String> ActivarCuenta (@RequestParam("codigo") String codigo){
        boolean activado = authServices.activarCuenta(codigo);

        if(activado){
            return ResponseEntity.ok("<h1>¡Cuenta activada!</h1><p>Ya puedes iniciar sesión en la app.</p>");
        }else {
            return ResponseEntity.badRequest().body("<h1>Error</h1><p>El código de activación es inválido o ya fue usado.</p>");
        }
    }
}
