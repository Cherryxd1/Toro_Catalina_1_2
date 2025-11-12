package com.muebleria.app.controller;

import com.muebleria.app.dto.CotizacionItemRequest;
import com.muebleria.app.dto.CotizacionRequest;
import com.muebleria.app.dto.CotizacionResponse;
import com.muebleria.app.service.CotizacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cotizaciones")
@RequiredArgsConstructor
public class CotizacionController {

    private final CotizacionService cotizacionService;

    @PostMapping
    public ResponseEntity<CotizacionResponse> crearCotizacion(@Valid @RequestBody CotizacionRequest request) {
        CotizacionResponse response = cotizacionService.crearCotizacion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{cotizacionId}/items")
    public ResponseEntity<CotizacionResponse> agregarItem(
            @PathVariable Long cotizacionId,
            @Valid @RequestBody CotizacionItemRequest request) {
        CotizacionResponse response = cotizacionService.agregarItem(cotizacionId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CotizacionResponse> obtenerPorId(@PathVariable Long id) {
        CotizacionResponse response = cotizacionService.obtenerPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CotizacionResponse>> listarTodas() {
        List<CotizacionResponse> cotizaciones = cotizacionService.listarTodas();
        return ResponseEntity.ok(cotizaciones);
    }

    @PostMapping("/{cotizacionId}/confirmar")
    public ResponseEntity<CotizacionResponse> confirmarVenta(@PathVariable Long cotizacionId) {
        CotizacionResponse response = cotizacionService.confirmarVenta(cotizacionId);
        return ResponseEntity.ok(response);
    }
}