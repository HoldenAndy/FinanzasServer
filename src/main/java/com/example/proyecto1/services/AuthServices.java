package com.example.proyecto1.services;

import com.example.proyecto1.daos.UsuarioDao;
import com.example.proyecto1.models.dtos.AuthResponse;
import com.example.proyecto1.models.dtos.LoginPeticion;
import com.example.proyecto1.models.dtos.RegisterPeticion;
import com.example.proyecto1.models.entities.Role;
import com.example.proyecto1.models.entities.Usuario;
import io.jsonwebtoken.Jwts;
import jakarta.mail.MessagingException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServices {
    private final UsuarioDao usuarioDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    public AuthServices(UsuarioDao usuarioDao, PasswordEncoder passwordEncoder, JwtService jwtService, EmailService emailService) {
        this.usuarioDao = usuarioDao;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }


    public void registrar(RegisterPeticion request){
        String codigo = UUID.randomUUID().toString();
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        usuario.setPassword(encodedPassword);
        usuario.setCodigoVerificacion(codigo);
        usuario.setActivado(false);
        usuario.setRole(Role.USER);
        usuarioDao.save(usuario);

        try {
            emailService.enviarEmailConfirmacion(usuario.getEmail(), codigo);
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }

    }

    public AuthResponse login (LoginPeticion request){
        Usuario usuario = usuarioDao.findByEmail(request.getEmail()).orElseThrow(() ->
            new RuntimeException("Credenciales invalidas."));

        if(!usuario.isActivado()){
            throw new RuntimeException("Tu cuenta no ha sido activada aún D:! Revisa tu correo: " +
                    usuario.getEmail());
        }

        if(!passwordEncoder.matches(request.getPassword(), usuario.getPassword())){
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(usuario);
        return new AuthResponse(token);
    }

    public boolean activarCuenta(String codigo){
        Optional<Usuario> usuarioVerificado = usuarioDao.buscarPorCodigoVerificacion(codigo);

        if(usuarioVerificado.isPresent()){
            Usuario usuario = usuarioVerificado.get();
            usuarioDao.ActivarUsuario(usuario.getId());
            return true;
        }
        return false;
    }

}
