package com.example.proyecto1.usuarios.servicesImpl;

import com.example.proyecto1.auth.dtos.ActualizarUsuarioPeticion;
import com.example.proyecto1.auth.dtos.UsuarioResponse;
import com.example.proyecto1.usuarios.daos.UsuarioDao;
import com.example.proyecto1.usuarios.entities.Usuario;
import com.example.proyecto1.usuarios.services.UsuarioService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioDao usuarioDao;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    public UsuarioServiceImpl(UsuarioDao usuarioDao, PasswordEncoder passwordEncoder, JavaMailSender mailSender) {
        this.usuarioDao = usuarioDao;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }


    @Override
    public UsuarioResponse obtenerUsuario(String email){
        var usuario = usuarioDao.findByEmail(email).orElseThrow(() ->
                new RuntimeException("Usuario no encontrado"));
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRole().toString()
        );

    }

    @Override
    @Transactional
    public UsuarioResponse actualizarPerfil(String email, ActualizarUsuarioPeticion peticion) {

        Usuario usuario = usuarioDao.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setNombre(peticion.nombre());
        if (peticion.password() != null && !peticion.password().isBlank()) {
            String passwordHash = passwordEncoder.encode(peticion.password());
            usuario.setPassword(passwordHash);
        }

        usuarioDao.actualizarUsuario(usuario);
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRole().toString()
        );
    }

    @Override
    public void cambiarPassword(String email, String passwordActual, String nuevaPassword) {
        Usuario usuario = usuarioDao.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (!passwordEncoder.matches(passwordActual, usuario.getPassword())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        String nuevaPasswordHash = passwordEncoder.encode(nuevaPassword);
        usuarioDao.actualizarPassword(usuario.getId(), nuevaPasswordHash);
    }

    @Override
    public void solicitarRecuperacion(String email) {
        usuarioDao.findByEmail(email).ifPresent(usuario -> {
            String token = UUID.randomUUID().toString();
            LocalDateTime expiracion = LocalDateTime.now().plusMinutes(15);
            usuarioDao.guardarTokenRecuperacion(email, token, expiracion);
            enviarEmailRecuperacion(email, token);
        });
    }

    @Override
    public void restablecerPassword(String token, String nuevaPassword) {
        Usuario usuario = usuarioDao.findByTokenRecuperacion(token)
                .orElseThrow(() -> new RuntimeException("Token inválido o no encontrado"));
        if (usuario.getTokenExpiracionPassword() == null || usuario.getTokenExpiracionPassword().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("El token ha expirado. Solicita uno nuevo.");
        }
        String passwordHash = passwordEncoder.encode(nuevaPassword);
        usuarioDao.actualizarPassword(usuario.getId(), passwordHash);
        usuarioDao.guardarTokenRecuperacion(usuario.getEmail(), null, null);
    }

    private void enviarEmailRecuperacion(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Recuperación de Contraseña - Finanzas App");
        message.setText("Hola,\n\n" +
                "Has solicitado restablecer tu contraseña.\n" +
                "Usa el siguiente token en la App: " + token + "\n\n" +
                "Ojo: Este token expira en 15 minutos.\n\n" +
                "Si no fuiste tú, ignora este mensaje.");

        mailSender.send(message);
    }
}