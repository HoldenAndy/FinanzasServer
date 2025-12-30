package com.example.proyecto1.security;

import com.example.proyecto1.daos.UsuarioDao;
import com.example.proyecto1.models.entities.Usuario;
import com.example.proyecto1.services.JwtService;
import jakarta.servlet.ServletException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.FilterChain;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UsuarioDao usuarioDao;

    public JwtAuthenticationFilter(JwtService jwtService, UsuarioDao usuarioDao) {
        this.jwtService = jwtService;
        this.usuarioDao = usuarioDao;
    }

        @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        final String usuarioEmail = jwtService.extracUsername(jwt);

        if(usuarioEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            Usuario usuario = this.usuarioDao.findByEmail(usuarioEmail).orElse(null);
            if(usuario != null && jwtService.isTokenValid(jwt, usuario)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(usuario,
                        null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}

