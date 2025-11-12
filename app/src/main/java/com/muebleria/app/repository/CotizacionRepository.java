package com.muebleria.app.repository;

import com.muebleria.app.domain.Cotizacion;
import com.muebleria.app.domain.EstadoCotizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CotizacionRepository extends JpaRepository<Cotizacion, Long> {
    List<Cotizacion> findByEstado(EstadoCotizacion estado);
}