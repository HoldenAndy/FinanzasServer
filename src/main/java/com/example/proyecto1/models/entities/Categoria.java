package com.example.proyecto1.models.entities;

public class Categoria {
    private Long id;
    private String nombre;
    private TipoMovimiento tipo; // INGRESO o EGRESO
    private Long usuarioId;

    public Categoria() {
    }

    public Categoria(Long id, String nombre, TipoMovimiento tipo, Long usuarioId) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.usuarioId = usuarioId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimiento tipo) {
        this.tipo = tipo;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}