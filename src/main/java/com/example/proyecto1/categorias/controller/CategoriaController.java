package com.example.proyecto1.categorias.controller;

import com.example.proyecto1.categorias.dtos.CategoriaPeticion;
import com.example.proyecto1.categorias.dtos.CategoriaRespuesta;
import com.example.proyecto1.categorias.entities.Categoria;
import com.example.proyecto1.categorias.services.CategoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/categorias")

public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> listarCategoria(Principal principal) {
        return ResponseEntity.ok(categoriaService.listarCategorias(principal.getName()));
    }

    @PostMapping
    public ResponseEntity<Void> crearCategoria(@RequestBody CategoriaPeticion peticion, Principal principal) {
        categoriaService.crearCategoria(peticion, principal.getName());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> editarCategoria(@PathVariable Long id, @RequestBody CategoriaPeticion peticion, Principal principal) {
        categoriaService.editarCategoria(id, peticion, principal.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id, Principal principal) {
        categoriaService.eliminarCategoria(id, principal.getName());
        return ResponseEntity.ok().build();
    }
}