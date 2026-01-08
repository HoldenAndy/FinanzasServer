package com.example.proyecto1.categorias.daos;

import com.example.proyecto1.categorias.dtos.CategoriaPeticion;
import com.example.proyecto1.categorias.entities.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaDao {

    void insertarCategoriasPorDefecto(Long usuarioId);

    void crearCategoria(Categoria categoria);

    void actualizarCategoria(Categoria categoria);

    void eliminarCategoria(Long id);

    Optional<Categoria> findById(Long id);

    List<Categoria> findAllByUsuarioId(Long usuarioId);

}
