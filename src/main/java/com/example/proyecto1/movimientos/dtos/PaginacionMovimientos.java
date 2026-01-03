package com.example.proyecto1.movimientos.dtos;

import java.util.List;

public record PaginacionMovimientos<T>(
   List<T> contenido,
   int paginaActual,
   int tamanioPagina,
   long totalElementos,
   int totalPaginas
){}
