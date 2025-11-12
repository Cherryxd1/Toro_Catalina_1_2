package com.muebleria.app.service;

import com.muebleria.app.domain.EstadoMueble;
import com.muebleria.app.domain.Mueble;
import com.muebleria.app.dto.MuebleRequest;
import com.muebleria.app.dto.MuebleResponse;
import com.muebleria.app.exception.RecursoNoEncontradoException;
import com.muebleria.app.repository.MuebleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogoService {
    
    private final MuebleRepository muebleRepository;
    
    @Transactional
    public MuebleResponse crearMueble(MuebleRequest request) {
        Mueble mueble = Mueble.builder()
                .nombre(request.getNombre())
                .tipo(request.getTipo())
                .precioBase(request.getPrecioBase())
                .stock(request.getStock())
                .estado(EstadoMueble.ACTIVO)
                .tamano(request.getTamano())
                .material(request.getMaterial())
                .build();
        
        Mueble guardado = muebleRepository.save(mueble);
        return convertirAResponse(guardado);
    }
    
    @Transactional(readOnly = true)
    public List<MuebleResponse> listarTodos() {
        return muebleRepository.findAll().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<MuebleResponse> listarActivos() {
        return muebleRepository.findByEstado(EstadoMueble.ACTIVO).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public MuebleResponse obtenerPorId(Long id) {
        Mueble mueble = muebleRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Mueble no encontrado con ID: " + id));
        return convertirAResponse(mueble);
    }
    
    @Transactional
    public MuebleResponse actualizarMueble(Long id, MuebleRequest request) {
        Mueble mueble = muebleRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Mueble no encontrado con ID: " + id));
        
        mueble.setNombre(request.getNombre());
        mueble.setTipo(request.getTipo());
        mueble.setPrecioBase(request.getPrecioBase());
        mueble.setStock(request.getStock());
        mueble.setTamano(request.getTamano());
        mueble.setMaterial(request.getMaterial());
        
        Mueble actualizado = muebleRepository.save(mueble);
        return convertirAResponse(actualizado);
    }
    
    @Transactional
    public void desactivarMueble(Long id) {
        Mueble mueble = muebleRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Mueble no encontrado con ID: " + id));
        
        mueble.desactivar();
        muebleRepository.save(mueble);
    }
    
    @Transactional
    public void activarMueble(Long id) {
        Mueble mueble = muebleRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Mueble no encontrado con ID: " + id));
        
        mueble.activar();
        muebleRepository.save(mueble);
    }
    
    // Metodo auxiliar para convertir entidad a DTO
    private MuebleResponse convertirAResponse(Mueble mueble) {
        return MuebleResponse.builder()
                .id(mueble.getId())
                .nombre(mueble.getNombre())
                .tipo(mueble.getTipo())
                .precioBase(mueble.getPrecioBase())
                .stock(mueble.getStock())
                .estado(mueble.getEstado())
                .tamano(mueble.getTamano())
                .material(mueble.getMaterial())
                .build();
    }
}