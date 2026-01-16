package com.example.proyecto1.auth.servicesImpl;

import com.example.proyecto1.auth.dtos.*;
import com.example.proyecto1.auth.services.AuthService;
import com.example.proyecto1.categorias.daos.CategoriaDao;
import com.example.proyecto1.email.services.EmailService;
import com.example.proyecto1.exceptions.NegocioException;
import com.example.proyecto1.jwt.services.JwtService;
import com.example.proyecto1.usuarios.daos.UsuarioDao;
import com.example.proyecto1.usuarios.entities.Role;
import com.example.proyecto1.usuarios.entities.Usuario;
import jakarta.mail.MessagingException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioDao usuarioDao;
    private final CategoriaDao categoriaDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    public AuthServiceImpl(UsuarioDao usuarioDao, CategoriaDao categoriaDao,
                          PasswordEncoder passwordEncoder, JwtService jwtService,
                          EmailService emailService) {
        this.usuarioDao = usuarioDao;
        this.categoriaDao = categoriaDao;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public void registrar(RegisterPeticion request) {
        if (usuarioDao.findByEmail(request.email()).isPresent()) {
            throw new NegocioException("El correo ya está registrado");
        }

        String codigo = UUID.randomUUID().toString();
        Usuario usuario = new Usuario();
        usuario.setNombre(request.nombre());
        usuario.setEmail(request.email());

        String encodedPassword = passwordEncoder.encode(request.password());
        usuario.setPassword(encodedPassword);
        usuario.setCodigoVerificacion(codigo);
        usuario.setCodigoVerificacionExpiracion(LocalDateTime.now().plusMinutes(15));
        usuario.setActivado(false);
        usuario.setRole(Role.USER);

        Long usuarioId = usuarioDao.insertarUsuario(usuario);
        categoriaDao.insertarCategoriasPorDefecto(usuarioId);

        try {
            emailService.enviarEmailConfirmacion(usuario.getEmail(), codigo);
        } catch (MessagingException e) {
            throw new NegocioException("Error al enviar el correo de confirmación. Inténtalo de nuevo.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginPeticion request) {
        Usuario usuario = usuarioDao.findByEmail(request.email())
                .orElseThrow(() -> new NegocioException("Credenciales inválidas."));

        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            throw new NegocioException("Credenciales inválidas");
        }

        if (!usuario.isActivado()) {
            throw new NegocioException("Tu cuenta no ha sido activada aún D:! Revisa tu correo: " +
                    usuario.getEmail());
        }

        String token = jwtService.generateToken(usuario);
        return new AuthResponse(token);
    }

    @Override
    @Transactional
    public boolean activarCuenta(String codigo) {
        Optional<Usuario> usuarioVerificado = usuarioDao.buscarPorCodigoVerificacion(codigo);
        if (usuarioVerificado.isPresent()) {
            Usuario usuario = usuarioVerificado.get();
            if (usuario.getCodigoVerificacionExpiracion() == null ||
                    LocalDateTime.now().isAfter(usuario.getCodigoVerificacionExpiracion())) {
                return false;
            }
            usuarioDao.ActivarUsuario(usuario.getId());
            return true;
        }
        return false;
    }

    @Override
    public UsuarioResponse obtenerPerfil(String email) {
        Usuario usuario = usuarioDao.findByEmail(email)
                .orElseThrow(() -> new NegocioException("Usuario no encontrado"));
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRole().name()
        );
    }

    @Override
    @Transactional
    public UsuarioResponse actualizarPerfil(String email, ActualizarUsuarioPeticion request) {
        Usuario usuario = usuarioDao.findByEmail(email)
                .orElseThrow(() -> new NegocioException("Usuario no encontrado"));

        usuario.setNombre(request.nombre());
        if (request.password() != null && !request.password().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(request.password()));
        }

        usuarioDao.actualizarUsuario(usuario);
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRole().name()
        );
    }
}