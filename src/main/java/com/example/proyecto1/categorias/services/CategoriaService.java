package com.example.proyecto1.categorias.services;

import com.example.proyecto1.categorias.dtos.CategoriaPeticion;
import com.example.proyecto1.categorias.dtos.CategoriaRespuesta;

import java.util.List;

public interface CategoriaService {
    List<CategoriaRespuesta> listarPorUsuario(String email);

    void crearCategoria(CategoriaPeticion peticion, String emailUsuario);

    void editarCategoria(Long id, CategoriaPeticion peticion, String emailUsuario);

    void eliminarCategoria(Long id, String emailUsuario);
}
