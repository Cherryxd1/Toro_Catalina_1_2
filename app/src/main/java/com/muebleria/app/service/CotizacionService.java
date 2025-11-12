package com.muebleria.app.service;

import com.muebleria.app.domain.*;
import com.muebleria.app.dto.*;
import com.muebleria.app.exception.RecursoNoEncontradoException;
import com.muebleria.app.exception.StockInsuficienteException;
import com.muebleria.app.repository.CotizacionRepository;
import com.muebleria.app.repository.MuebleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CotizacionService {

    private final CotizacionRepository cotizacionRepository;
    private final MuebleRepository muebleRepository;
    private final VarianteService varianteService;
    private final PrecioService precioService;

    @Transactional
    public CotizacionResponse crearCotizacion(CotizacionRequest request) {
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setClienteNombre(request.getClienteNombre());
        cotizacion.setFecha(LocalDateTime.now());
        cotizacion.setEstado(EstadoCotizacion.PENDIENTE);
        cotizacion.setTotal(0.0);

        Cotizacion guardada = cotizacionRepository.save(cotizacion);
        return convertirAResponse(guardada);
    }

    @Transactional
    public CotizacionResponse agregarItem(Long cotizacionId, CotizacionItemRequest request) {
        // Buscar cotizacion
        Cotizacion cotizacion = cotizacionRepository.findById(cotizacionId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cotizacion no encontrada con ID: " + cotizacionId));

        // Verificar que no este confirmada
        if (cotizacion.getEstado() == EstadoCotizacion.CONFIRMADA) {
            throw new IllegalStateException("No se puede modificar una cotizacion confirmada");
        }

        // Buscar mueble
        Mueble mueble = muebleRepository.findById(request.getMuebleId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Mueble no encontrado con ID: " + request.getMuebleId()));

        // Buscar variante (puede ser null)
        Variante variante = null;
        if (request.getVarianteId() != null) {
            variante = varianteService.obtenerEntidadPorId(request.getVarianteId());
        }

        // Calcular precio usando el servicio de precios (patron Strategy)
        Double precioCalculado = precioService.calcularPrecioFinal(mueble, variante);

        // Crear el item
        CotizacionItem item = new CotizacionItem();
        item.setMueble(mueble);
        item.setVariante(variante);
        item.setCantidad(request.getCantidad());
        item.calcularPrecioUnitario(precioCalculado);

        // Agregar a la cotizacion
        cotizacion.agregarItem(item);
        cotizacion.calcularTotal();

        Cotizacion actualizada = cotizacionRepository.save(cotizacion);
        return convertirAResponse(actualizada);
    }

    @Transactional(readOnly = true)
    public CotizacionResponse obtenerPorId(Long id) {
        Cotizacion cotizacion = cotizacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cotizacion no encontrada con ID: " + id));
        return convertirAResponse(cotizacion);
    }

    @Transactional(readOnly = true)
    public List<CotizacionResponse> listarTodas() {
        return cotizacionRepository.findAll().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CotizacionResponse confirmarVenta(Long cotizacionId) {
        Cotizacion cotizacion = cotizacionRepository.findById(cotizacionId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cotizacion no encontrada con ID: " + cotizacionId));

        // Verificar que no este ya confirmada
        if (cotizacion.getEstado() == EstadoCotizacion.CONFIRMADA) {
            throw new IllegalStateException("La cotizacion ya fue confirmada");
        }

        // Verificar stock de todos los items ANTES de confirmar
        for (CotizacionItem item : cotizacion.getItems()) {
            Mueble mueble = item.getMueble();
            if (!mueble.tieneStock(item.getCantidad())) {
                throw new StockInsuficienteException("stock insuficiente");
            }
        }

        // Si llego aqui, hay stock suficiente para todos. Reducir stock
        for (CotizacionItem item : cotizacion.getItems()) {
            Mueble mueble = item.getMueble();
            mueble.reducirStock(item.getCantidad());
            muebleRepository.save(mueble);
        }

        // Confirmar la cotizacion
        cotizacion.confirmar();
        Cotizacion confirmada = cotizacionRepository.save(cotizacion);

        return convertirAResponse(confirmada);
    }

    private CotizacionResponse convertirAResponse(Cotizacion cotizacion) {
        List<CotizacionItemResponse> itemsResponse = cotizacion.getItems().stream()
                .map(this::convertirItemAResponse)
                .collect(Collectors.toList());

        return CotizacionResponse.builder()
                .id(cotizacion.getId())
                .fecha(cotizacion.getFecha())
                .estado(cotizacion.getEstado())
                .clienteNombre(cotizacion.getClienteNombre())
                .total(cotizacion.getTotal())
                .items(itemsResponse)
                .build();
    }

    private CotizacionItemResponse convertirItemAResponse(CotizacionItem item) {
        return CotizacionItemResponse.builder()
                .id(item.getId())
                .muebleNombre(item.getMueble().getNombre())
                .varianteNombre(item.getVariante() != null ? item.getVariante().getNombre() : "NORMAL")
                .cantidad(item.getCantidad())
                .precioUnitario(item.getPrecioUnitario())
                .subtotal(item.getSubtotal())
                .build();
    }
}