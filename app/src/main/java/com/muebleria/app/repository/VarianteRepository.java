package com.muebleria.app.repository;

import com.muebleria.app.domain.Variante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VarianteRepository extends JpaRepository<Variante, Long> {
    Optional<Variante> findByNombre(String nombre);
}