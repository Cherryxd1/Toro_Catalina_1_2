package com.muebleria.app.service;

import com.muebleria.app.domain.*;
import com.muebleria.app.dto.CotizacionItemRequest;
import com.muebleria.app.dto.CotizacionRequest;
import com.muebleria.app.dto.CotizacionResponse;
import com.muebleria.app.exception.StockInsuficienteException;
import com.muebleria.app.repository.CotizacionRepository;
import com.muebleria.app.repository.MuebleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("Pruebas de Stock y Ventas")
class CotizacionServiceTest {

    @Autowired
    private CotizacionService cotizacionService;

    @Autowired
    private MuebleRepository muebleRepository;

    @Autowired
    private CotizacionRepository cotizacionRepository;

    private Mueble mueble;

    @BeforeEach
    void setUp() {
        // Limpiar la base de datos antes de cada test
        cotizacionRepository.deleteAll();
        muebleRepository.deleteAll();

        // Crear un mueble de prueba con stock limitado
        mueble = new Mueble();
        mueble.setNombre("Silla de Prueba");
        mueble.setTipo("silla");
        mueble.setPrecioBase(10000.0);
        mueble.setStock(5); // Solo 5 en stock
        mueble.setEstado(EstadoMueble.ACTIVO);
        mueble.setTamano(TamanoMueble.MEDIANO);
        mueble.setMaterial("madera");

        mueble = muebleRepository.save(mueble);
    }

    @Test
    @DisplayName("Confirmar venta con stock suficiente debe reducir el stock correctamente")
    void confirmarVentaConStockSuficiente() {
        // Given: una cotizacion con 3 unidades (hay 5 en stock)
        CotizacionRequest cotizacionRequest = new CotizacionRequest();
        cotizacionRequest.setClienteNombre("Juan Perez");

        CotizacionResponse cotizacion = cotizacionService.crearCotizacion(cotizacionRequest);

        CotizacionItemRequest itemRequest = new CotizacionItemRequest();
        itemRequest.setMuebleId(mueble.getId());
        itemRequest.setVarianteId(null);
        itemRequest.setCantidad(3);

        cotizacionService.agregarItem(cotizacion.getId(), itemRequest);

        // When: confirmamos la venta
        cotizacionService.confirmarVenta(cotizacion.getId());

        // Then: el stock debe reducirse de 5 a 2
        Mueble muebleActualizado = muebleRepository.findById(mueble.getId()).orElseThrow();
        assertEquals(2, muebleActualizado.getStock());

        // Y la cotizacion debe estar confirmada
        Cotizacion cotizacionConfirmada = cotizacionRepository.findById(cotizacion.getId()).orElseThrow();
        assertEquals(EstadoCotizacion.CONFIRMADA, cotizacionConfirmada.getEstado());
    }

    @Test
    @DisplayName("Confirmar venta con stock insuficiente debe lanzar excepcion")
    void confirmarVentaConStockInsuficiente() {
        // Given: una cotizacion con 10 unidades (solo hay 5 en stock)
        CotizacionRequest cotizacionRequest = new CotizacionRequest();
        cotizacionRequest.setClienteNombre("Maria Lopez");

        CotizacionResponse cotizacion = cotizacionService.crearCotizacion(cotizacionRequest);

        CotizacionItemRequest itemRequest = new CotizacionItemRequest();
        itemRequest.setMuebleId(mueble.getId());
        itemRequest.setVarianteId(null);
        itemRequest.setCantidad(10); // Mas de lo disponible

        cotizacionService.agregarItem(cotizacion.getId(), itemRequest);

        // When & Then: al confirmar debe lanzar StockInsuficienteException
        StockInsuficienteException exception = assertThrows(
                StockInsuficienteException.class,
                () -> cotizacionService.confirmarVenta(cotizacion.getId())
        );

        // Verificar el mensaje exacto
        assertEquals("stock insuficiente", exception.getMessage());

        // Verificar que el stock NO se haya modificado
        Mueble muebleSinCambios = muebleRepository.findById(mueble.getId()).orElseThrow();
        assertEquals(5, muebleSinCambios.getStock());
    }

    @Test
    @DisplayName("Confirmar venta con exactamente el stock disponible debe funcionar")
    void confirmarVentaConStockExacto() {
        // Given: una cotizacion con exactamente 5 unidades (el stock completo)
        CotizacionRequest cotizacionRequest = new CotizacionRequest();
        cotizacionRequest.setClienteNombre("Pedro Garcia");

        CotizacionResponse cotizacion = cotizacionService.crearCotizacion(cotizacionRequest);

        CotizacionItemRequest itemRequest = new CotizacionItemRequest();
        itemRequest.setMuebleId(mueble.getId());
        itemRequest.setVarianteId(null);
        itemRequest.setCantidad(5);

        cotizacionService.agregarItem(cotizacion.getId(), itemRequest);

        // When: confirmamos la venta
        cotizacionService.confirmarVenta(cotizacion.getId());

        // Then: el stock debe quedar en 0
        Mueble muebleActualizado = muebleRepository.findById(mueble.getId()).orElseThrow();
        assertEquals(0, muebleActualizado.getStock());
    }
}