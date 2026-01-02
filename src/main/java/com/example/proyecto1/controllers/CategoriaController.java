package com.example.proyecto1.controllers;

import com.example.proyecto1.models.dtos.CategoriaRespuesta;
import com.example.proyecto1.models.entities.Categoria;
import com.example.proyecto1.services.CategoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/categorias") // Usamos /api para ser consistentes
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaRespuesta>> listarMisCategorias(Principal principal) {
        List<CategoriaRespuesta> categorias = categoriaService.listarPorUsuario(principal.getName());
        return ResponseEntity.ok(categorias);
    }
}