package com.example.proyecto1.aiApi.services;

import org.springframework.stereotype.Repository;

@Repository
public interface FinancialContextService {
    String obtenerContextoFinanciero(String email);

}
