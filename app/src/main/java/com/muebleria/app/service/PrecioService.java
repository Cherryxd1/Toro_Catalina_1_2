package com.muebleria.app.service;

import com.muebleria.app.domain.Mueble;
import com.muebleria.app.domain.Variante;
import com.muebleria.app.service.price.PrecioConVarianteStrategy;
import com.muebleria.app.service.price.PrecioNormalStrategy;
import com.muebleria.app.service.price.PrecioStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrecioService {
    
    private final PrecioNormalStrategy precioNormalStrategy;
    private final PrecioConVarianteStrategy precioConVarianteStrategy;
    
    /**
     * Calcula el precio final de un mueble con o sin variante
     * Aqui se aplica el patron Strategy
     */
    public Double calcularPrecioFinal(Mueble mueble, Variante variante) {
        PrecioStrategy strategy;
        
        // Decidir que estrategia usar
        if (variante == null || variante.getEsNormal()) {
            strategy = precioNormalStrategy;
        } else {
            strategy = precioConVarianteStrategy;
        }
        
        // Ejecutar la estrategia seleccionada
        return strategy.calcularPrecio(mueble, variante);
    }
}