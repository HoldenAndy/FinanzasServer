package com.example.proyecto1.services;

import com.example.proyecto1.models.dtos.CategoriaRespuesta;
import com.example.proyecto1.models.entities.Categoria;

import java.util.List;

public interface CategoriaService {
    List<CategoriaRespuesta> listarPorUsuario(String email);
}
