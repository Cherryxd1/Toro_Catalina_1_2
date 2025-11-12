package com.muebleria.app.service.price;

import com.muebleria.app.domain.Mueble;
import com.muebleria.app.domain.Variante;
import org.springframework.stereotype.Component;

@Component
public class PrecioConVarianteStrategy implements PrecioStrategy {
    
    @Override
    public Double calcularPrecio(Mueble mueble, Variante variante) {
        Double precioBase = mueble.getPrecioBase();
        
        // Si la variante es normal, no modifica el precio
        if (variante.getEsNormal()) {
            return precioBase;
        }
        
        // Aplicar incremento por porcentaje
        Double incrementoPorcentaje = precioBase * (variante.getPorcentajeIncremento() / 100.0);
        
        // Aplicar monto fijo adicional
        Double montoFijo = variante.getMontoFijo();
        
        // Precio final = base + incremento porcentual + monto fijo
        return precioBase + incrementoPorcentaje + montoFijo;
    }
}