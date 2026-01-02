package com.example.proyecto1.services;

import com.example.proyecto1.daos.CategoriaDao;
import com.example.proyecto1.daos.UsuarioDao;
import com.example.proyecto1.models.dtos.CategoriaRespuesta;
import com.example.proyecto1.models.entities.Categoria;
import com.example.proyecto1.models.entities.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {
    private final CategoriaDao categoriaDao;
    private final UsuarioDao usuarioDao; // Para buscar el ID por email

    public CategoriaServiceImpl(CategoriaDao categoriaDao, UsuarioDao usuarioDao) {
        this.categoriaDao = categoriaDao;
        this.usuarioDao = usuarioDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaRespuesta> listarPorUsuario(String email) {
        Usuario usuario = usuarioDao.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        List<Categoria> categoriasEntity = categoriaDao.findAllByUsuarioId(usuario.getId());
        return categoriasEntity.stream()
                .map(cat -> new CategoriaRespuesta(
                        cat.getId(),
                        cat.getNombre(),
                        cat.getTipo()
                ))
                .toList();
    }
}