package com.example.proyecto1.presupuestos.entities;

import com.example.proyecto1.movimientos.entities.Moneda;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Presupuesto {
    private Long id;
    private Long usuarioId;
    private Long categoriaId;
    private String nombreCategoria;
    private BigDecimal montoLimite;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Moneda moneda;

    public Presupuesto() {
    }

    public Presupuesto(Long id, Long usuarioId, Long categoriaId, String nombreCategoria, BigDecimal montoLimite, LocalDate fechaInicio, LocalDate fechaFin, Moneda moneda) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.categoriaId = categoriaId;
        this.nombreCategoria = nombreCategoria;
        this.montoLimite = montoLimite;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.moneda = moneda;
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

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public BigDecimal getMontoLimite() {
        return montoLimite;
    }

    public void setMontoLimite(BigDecimal montoLimite) {
        this.montoLimite = montoLimite;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }
}