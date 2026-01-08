package com.example.proyecto1.categorias.servicesImpl;

import com.example.proyecto1.categorias.daos.CategoriaDao;
import com.example.proyecto1.categorias.services.CategoriaService;
import com.example.proyecto1.usuarios.daos.UsuarioDao;
import com.example.proyecto1.categorias.dtos.CategoriaRespuesta;
import com.example.proyecto1.categorias.entities.Categoria;
import com.example.proyecto1.usuarios.entities.Usuario;
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
    public List<CategoriaRespuesta> listarPorUsuario(String email) {
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaRespuesta> listarPorUsuario(String email) {
        Usuario usuario = usuarioDao.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
<<<<<<< Updated upstream
        List<Categoria> categoriasEntity = categoriaDao.findAllByUsuarioId(usuario.getId());
        return categoriasEntity.stream()
                .map(cat -> new CategoriaRespuesta(
                        cat.getId(),
                        cat.getNombre(),
                        cat.getTipo()
                ))
                .toList();
=======
        return categoriaDao.findAllByUsuarioId(usuario.getId());
    }

    @Override
    @Transactional
    public void crearCategoria(CategoriaPeticion peticion, String emailUsuario) {
        Usuario usuario = usuarioDao.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombre(peticion.nombre());
        nuevaCategoria.setTipo(peticion.tipo());
        nuevaCategoria.setUsuarioId(usuario.getId());
        categoriaDao.crearCategoria(nuevaCategoria);
    }

    @Override
    @Transactional
    public void editarCategoria(Long id, CategoriaPeticion peticion, String emailUsuario) {
        Categoria categoria = categoriaDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Usuario usuario = usuarioDao.findByEmail(emailUsuario).get();
        if (!categoria.getUsuarioId().equals(usuario.getId())) {
            throw new RuntimeException("No tienes permiso para editar esta categoría");
        }

        // 3. Actualizamos los datos
        categoria.setNombre(peticion.nombre());
        categoria.setTipo(peticion.tipo());

        categoriaDao.actualizarCategoria(categoria);
    }

    @Override
    @Transactional
    public void eliminarCategoria(Long id, String emailUsuario) {
        Categoria categoria = categoriaDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Usuario usuario = usuarioDao.findByEmail(emailUsuario).get();
        if (!categoria.getUsuarioId().equals(usuario.getId())) {
            throw new RuntimeException("No tienes permiso para eliminar esta categoría");
        }

        categoriaDao.eliminarCategoria(id);
>>>>>>> Stashed changes
    }
}