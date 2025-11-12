package com.muebleria.app.controller;

import com.muebleria.app.dto.MuebleRequest;
import com.muebleria.app.dto.MuebleResponse;
import com.muebleria.app.service.CatalogoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/muebles")
@RequiredArgsConstructor
public class MuebleController {
    
    private final CatalogoService catalogoService;
    
    @PostMapping
    public ResponseEntity<MuebleResponse> crearMueble(@Valid @RequestBody MuebleRequest request) {
        MuebleResponse response = catalogoService.crearMueble(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    public ResponseEntity<List<MuebleResponse>> listarTodos() {
        List<MuebleResponse> muebles = catalogoService.listarTodos();
        return ResponseEntity.ok(muebles);
    }
    
    @GetMapping("/activos")
    public ResponseEntity<List<MuebleResponse>> listarActivos() {
        List<MuebleResponse> muebles = catalogoService.listarActivos();
        return ResponseEntity.ok(muebles);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MuebleResponse> obtenerPorId(@PathVariable Long id) {
        MuebleResponse response = catalogoService.obtenerPorId(id);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<MuebleResponse> actualizarMueble(
            @PathVariable Long id,
            @Valid @RequestBody MuebleRequest request) {
        MuebleResponse response = catalogoService.actualizarMueble(id, request);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarMueble(@PathVariable Long id) {
        catalogoService.desactivarMueble(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/activar")
    public ResponseEntity<Void> activarMueble(@PathVariable Long id) {
        catalogoService.activarMueble(id);
        return ResponseEntity.noContent().build();
    }
}