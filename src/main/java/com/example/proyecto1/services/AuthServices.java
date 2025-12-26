package com.example.proyecto1.services;

import com.example.proyecto1.daos.UsuarioDao;
import com.example.proyecto1.models.entities.Role;
import com.example.proyecto1.models.entities.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServices {
    private final UsuarioDao usuarioDao;
    private final PasswordEncoder passwordEncoder;

    public AuthServices(UsuarioDao usuarioDao, PasswordEncoder passwordEncoder) {
        this.usuarioDao = usuarioDao;
        this.passwordEncoder = passwordEncoder;
    }

    public void registrar(Usuario usuario){
        String encodedPassword = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(encodedPassword);

        if(usuario.getRole() == null){
            usuario.setRole(Role.USER);
        }
        usuarioDao.save(usuario);
    }
}
