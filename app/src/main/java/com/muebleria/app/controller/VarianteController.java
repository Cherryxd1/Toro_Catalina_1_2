package com.muebleria.app.controller;

import com.muebleria.app.dto.VarianteRequest;
import com.muebleria.app.dto.VarianteResponse;
import com.muebleria.app.service.VarianteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/variantes")
@RequiredArgsConstructor
public class VarianteController {
    
    private final VarianteService varianteService;
    
    @PostMapping
    public ResponseEntity<VarianteResponse> crearVariante(@Valid @RequestBody VarianteRequest request) {
        VarianteResponse response = varianteService.crearVariante(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/tipo/{tipo}")
    public ResponseEntity<VarianteResponse> crearVariantePorTipo(@PathVariable String tipo) {
        VarianteResponse response = varianteService.crearVariantePorTipo(tipo);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    public ResponseEntity<List<VarianteResponse>> listarTodas() {
        List<VarianteResponse> variantes = varianteService.listarTodas();
        return ResponseEntity.ok(variantes);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<VarianteResponse> obtenerPorId(@PathVariable Long id) {
        VarianteResponse response = varianteService.obtenerPorId(id);
        return ResponseEntity.ok(response);
    }
}