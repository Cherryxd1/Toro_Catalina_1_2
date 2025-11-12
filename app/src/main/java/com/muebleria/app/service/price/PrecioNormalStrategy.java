package com.muebleria.app.service.price;

import com.muebleria.app.domain.Mueble;
import com.muebleria.app.domain.Variante;
import org.springframework.stereotype.Component;

@Component
public class PrecioNormalStrategy implements PrecioStrategy {
    
    @Override
    public Double calcularPrecio(Mueble mueble, Variante variante) {
        // Sin variante, devuelve el precio base
        return mueble.getPrecioBase();
    }
}