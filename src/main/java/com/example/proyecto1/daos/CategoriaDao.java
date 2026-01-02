package com.example.proyecto1.daos;

import com.example.proyecto1.models.entities.Categoria;

import java.util.List;

public interface CategoriaDao {
        void insertarCategoriasPorDefecto(Long usuarioId);
        List<Categoria> findAllByUsuarioId(Long usuarioId);
    }
