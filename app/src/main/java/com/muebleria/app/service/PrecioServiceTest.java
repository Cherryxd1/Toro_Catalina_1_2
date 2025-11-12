package com.muebleria.app.service;

import com.muebleria.app.domain.Mueble;
import com.muebleria.app.domain.TamanoMueble;
import com.muebleria.app.domain.Variante;
import com.muebleria.app.service.price.PrecioConVarianteStrategy;
import com.muebleria.app.service.price.PrecioNormalStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas del Servicio de Precios")
class PrecioServiceTest {
    
    private PrecioService precioService;
    private Mueble mueble;
    
    @BeforeEach
    void setUp() {
        PrecioNormalStrategy normalStrategy = new PrecioNormalStrategy();
        PrecioConVarianteStrategy conVarianteStrategy = new PrecioConVarianteStrategy();
        precioService = new PrecioService(normalStrategy, conVarianteStrategy);
        
        // Crear un mueble de prueba
        mueble = Mueble.builder()
                .id(1L)
                .nombre("Silla de Prueba")
                .tipo("silla")
                .precioBase(10000.0)
                .stock(10)
                .tamano(TamanoMueble.MEDIANO)
                .material("madera")
                .build();
    }
    
    @Test
    @DisplayName("Calcular precio sin variante debe devolver precio base")
    void calcularPrecioSinVariante() {
        // Given: un mueble sin variante
        
        // When: calculamos el precio
        Double precioFinal = precioService.calcularPrecioFinal(mueble, null);
        
        // Then: debe ser igual al precio base
        assertEquals(10000.0, precioFinal);
    }
    
    @Test
    @DisplayName("Calcular precio con variante normal debe devolver precio base")
    void calcularPrecioConVarianteNormal() {
        // Given: una variante normal
        Variante varianteNormal = Variante.builder()
                .id(1L)
                .nombre("NORMAL")
                .descripcion("Sin variante")
                .porcentajeIncremento(0.0)
                .montoFijo(0.0)
                .esNormal(true)
                .build();
        
        // When: calculamos el precio
        Double precioFinal = precioService.calcularPrecioFinal(mueble, varianteNormal);
        
        // Then: debe ser igual al precio base
        assertEquals(10000.0, precioFinal);
    }
    
    @Test
    @DisplayName("Calcular precio con variante que aumenta 20% debe incrementar correctamente")
    void calcularPrecioConVariantePorcentaje() {
        // Given: una variante que aumenta 20%
        Variante variante = Variante.builder()
                .id(2L)
                .nombre("BARNIZ_PREMIUM")
                .descripcion("Barniz premium")
                .porcentajeIncremento(20.0)
                .montoFijo(0.0)
                .esNormal(false)
                .build();
        
        // When: calculamos el precio
        Double precioFinal = precioService.calcularPrecioFinal(mueble, variante);
        
        // Then: debe ser 10000 + 20% = 12000
        assertEquals(12000.0, precioFinal);
    }
    
    @Test
    @DisplayName("Calcular precio con variante que suma porcentaje y monto fijo")
    void calcularPrecioConVarianteMixta() {
        // Given: una variante con 25% + 5000 pesos
        Variante variante = Variante.builder()
                .id(3L)
                .nombre("COJINES_SEDA")
                .descripcion("Cojines de seda")
                .porcentajeIncremento(25.0)
                .montoFijo(5000.0)
                .esNormal(false)
                .build();
        
        // When: calculamos el precio
        Double precioFinal = precioService.calcularPrecioFinal(mueble, variante);
        
        // Then: debe ser 10000 + 25% (2500) + 5000 = 17500
        assertEquals(17500.0, precioFinal);

        
    }
}