package com.muebleria.app.repository;

import com.muebleria.app.domain.EstadoMueble;
import com.muebleria.app.domain.Mueble;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MuebleRepository extends JpaRepository<Mueble, Long> {
    List<Mueble> findByEstado(EstadoMueble estado);
    List<Mueble> findByTipo(String tipo);
}