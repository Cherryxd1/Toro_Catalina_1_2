package com.muebleria.app.factory;

import com.muebleria.app.domain.Variante;
import org.springframework.stereotype.Component;

@Component
public class VarianteFactory {
    
    public Variante crearVariante(String tipo) {
        switch (tipo.toUpperCase()) {
            case "BARNIZ_PREMIUM":
                return Variante.builder()
                        .nombre("BARNIZ_PREMIUM")
                        .descripcion("Barniz premium de alta calidad")
                        .porcentajeIncremento(15.0)
                        .montoFijo(0.0)
                        .esNormal(false)
                        .build();
                
            case "COJINES_SEDA":
                return Variante.builder()
                        .nombre("COJINES_SEDA")
                        .descripcion("Cojines de seda suave y elegante")
                        .porcentajeIncremento(25.0)
                        .montoFijo(5000.0)
                        .esNormal(false)
                        .build();
                
            case "RUEDAS":
                return Variante.builder()
                        .nombre("RUEDAS")
                        .descripcion("Ruedas giratorias de alta resistencia")
                        .porcentajeIncremento(10.0)
                        .montoFijo(3000.0)
                        .esNormal(false)
                        .build();
                
            case "NORMAL":
                return Variante.builder()
                        .nombre("NORMAL")
                        .descripcion("Sin variante especial")
                        .porcentajeIncremento(0.0)
                        .montoFijo(0.0)
                        .esNormal(true)
                        .build();
                
            default:
                throw new IllegalArgumentException("Tipo de variante desconocido: " + tipo);
        }
    }
}