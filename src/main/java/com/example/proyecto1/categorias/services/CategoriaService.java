package com.example.proyecto1.categorias.services;

import com.example.proyecto1.categorias.dtos.CategoriaPeticion;
import com.example.proyecto1.categorias.dtos.CategoriaRespuesta;
import com.example.proyecto1.categorias.entities.Categoria;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CategoriaService {

    List<Categoria> listarCategorias(String emailUsuario);

    void crearCategoria(CategoriaPeticion peticion, String emailUsuario);

    void editarCategoria(Long id, CategoriaPeticion peticion, String emailUsuario);

    void eliminarCategoria(Long id, String emailUsuario);
}
