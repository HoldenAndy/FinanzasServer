package com.example.proyecto1.auth.servicesImpl;

import com.example.proyecto1.auth.dtos.*;
import com.example.proyecto1.auth.services.AuthService;
import com.example.proyecto1.categorias.daos.CategoriaDao;
import com.example.proyecto1.email.services.EmailService;
import com.example.proyecto1.jwt.services.JwtService;
import com.example.proyecto1.usuarios.daos.UsuarioDao;
import com.example.proyecto1.usuarios.entities.Role;
import com.example.proyecto1.usuarios.entities.Usuario;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    private final UsuarioDao usuarioDao;
    private final CategoriaDao categoriaDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    public AuthServiceImpl(UsuarioDao usuarioDao, CategoriaDao categoriaDao, PasswordEncoder passwordEncoder, JwtService jwtService, EmailService emailService) {
        this.usuarioDao = usuarioDao;
        this.categoriaDao = categoriaDao;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public void registrar(RegisterPeticion request){
        String codigo = UUID.randomUUID().toString();
        Usuario usuario = new Usuario();
        usuario.setNombre(request.nombre());
        usuario.setEmail(request.email());
        String encodedPassword = passwordEncoder.encode(request.password());
        usuario.setPassword(encodedPassword);
        usuario.setCodigoVerificacion(codigo);
        usuario.setActivado(false);
        usuario.setRole(Role.USER);
        Long usuarioId = usuarioDao.insertarUsuario(usuario);
        categoriaDao.insertarCategoriasPorDefecto(usuarioId);

        try {
            emailService.enviarEmailConfirmacion(usuario.getEmail(), codigo);
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    @Transactional
    public AuthResponse login (LoginPeticion request){
        Usuario usuario = usuarioDao.findByEmail(request.email()).orElseThrow(() ->
            new RuntimeException("Credenciales invalidas."));

        if(!usuario.isActivado()){
            throw new RuntimeException("Tu cuenta no ha sido activada aún D:! Revisa tu correo: " +
                    usuario.getEmail());
        }

        if(!passwordEncoder.matches(request.password(), usuario.getPassword())){
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(usuario);
        return new AuthResponse(token);
    }

    @Override
    @Transactional
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

