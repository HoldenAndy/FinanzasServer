package com.example.proyecto1.usuarios.entities;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class Usuario implements UserDetails{

    private Long id;
    private String email;
    private String password;
    private String nombre;
    private Role role;
    private String codigoVerificacion;
    private boolean activado;
    private String tokenRecuperacionPassword;
    private LocalDateTime tokenExpiracionPassword;

    public Usuario() {
    }

    public Usuario(Long id, String email, String password, String nombre, Role role, String codigoVerificacion,
                   boolean activado, String tokenRecuperacionPassword, LocalDateTime tokenExpiracionPassword) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.role = role;
        this.codigoVerificacion = codigoVerificacion;
        this.activado = activado;
        this.tokenRecuperacionPassword = tokenRecuperacionPassword;
        this.tokenExpiracionPassword = tokenExpiracionPassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getCodigoVerificacion() {
        return codigoVerificacion;
    }

    public void setCodigoVerificacion(String codigoVerificacion) {
        this.codigoVerificacion = codigoVerificacion;
    }

    public boolean isActivado() {
        return activado;
    }

    public void setActivado(boolean activado) {
        this.activado = activado;
    }

    public String getTokenRecuperacionPassword() {
        return tokenRecuperacionPassword;
    }

    public void setTokenRecuperacionPassword(String tokenRecuperacionPassword) {
        this.tokenRecuperacionPassword = tokenRecuperacionPassword;
    }

    public LocalDateTime getTokenExpiracionPassword() {
        return tokenExpiracionPassword;
    }

    public void setTokenExpiracionPassword(LocalDateTime tokenExpiracionPassword) {
        this.tokenExpiracionPassword = tokenExpiracionPassword;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.activado;
    }
}
