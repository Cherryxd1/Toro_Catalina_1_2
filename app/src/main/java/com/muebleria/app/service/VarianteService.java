package com.muebleria.app.service;

import com.muebleria.app.domain.Variante;
import com.muebleria.app.dto.VarianteRequest;
import com.muebleria.app.dto.VarianteResponse;
import com.muebleria.app.exception.RecursoNoEncontradoException;
import com.muebleria.app.factory.VarianteFactory;
import com.muebleria.app.repository.VarianteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VarianteService {
    
    private final VarianteRepository varianteRepository;
    private final VarianteFactory varianteFactory;
    
    @Transactional
    public VarianteResponse crearVariante(VarianteRequest request) {
        Variante variante = Variante.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .porcentajeIncremento(request.getPorcentajeIncremento())
                .montoFijo(request.getMontoFijo())
                .esNormal(request.getEsNormal())
                .build();
        
        Variante guardada = varianteRepository.save(variante);
        return convertirAResponse(guardada);
    }
    
    @Transactional
    public VarianteResponse crearVariantePorTipo(String tipo) {
        // Usar la Factory para crear la variante
        Variante variante = varianteFactory.crearVariante(tipo);
        
        // Verificar si ya existe
        if (varianteRepository.findByNombre(variante.getNombre()).isPresent()) {
            throw new IllegalArgumentException("La variante " + tipo + " ya existe");
        }
        
        Variante guardada = varianteRepository.save(variante);
        return convertirAResponse(guardada);
    }
    
    @Transactional(readOnly = true)
    public List<VarianteResponse> listarTodas() {
        return varianteRepository.findAll().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public VarianteResponse obtenerPorId(Long id) {
        Variante variante = varianteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Variante no encontrada con ID: " + id));
        return convertirAResponse(variante);
    }
    
    @Transactional(readOnly = true)
    public Variante obtenerEntidadPorId(Long id) {
        return varianteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Variante no encontrada con ID: " + id));
    }
    
    private VarianteResponse convertirAResponse(Variante variante) {
        return VarianteResponse.builder()
                .id(variante.getId())
                .nombre(variante.getNombre())
                .descripcion(variante.getDescripcion())
                .porcentajeIncremento(variante.getPorcentajeIncremento())
                .montoFijo(variante.getMontoFijo())
                .esNormal(variante.getEsNormal())
                .build();
    }
}