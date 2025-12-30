package com.example.proyecto1.models.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Movimiento {
    private Long id;
    private Long usuarioId;
    private Long categoriaId;
    private BigDecimal monto;
    private LocalDate fecha;
    private String descripcion;
    private TipoMovimiento tipo;

    public Movimiento() {
    }

    public Movimiento(Long id, Long usuarioId, Long categoriaId, BigDecimal monto, LocalDate fecha, String descripcion, TipoMovimiento tipo) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.categoriaId = categoriaId;
        this.monto = monto;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimiento tipo) {
        this.tipo = tipo;
    }
}
