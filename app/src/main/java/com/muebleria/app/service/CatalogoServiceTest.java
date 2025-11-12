package com.muebleria.app.service;

import com.muebleria.app.domain.EstadoMueble;
import com.muebleria.app.domain.TamanoMueble;
import com.muebleria.app.dto.MuebleRequest;
import com.muebleria.app.dto.MuebleResponse;
import com.muebleria.app.exception.RecursoNoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("Pruebas de Gestion de Catalogo (CRUD)")
class CatalogoServiceTest {
    
    @Autowired
    private CatalogoService catalogoService;
    
    @Test
    @DisplayName("Crear mueble debe guardar correctamente")
    void crearMueble() {
        // Given: datos para un nuevo mueble
        MuebleRequest request = MuebleRequest.builder()
                .nombre("Silla Nueva")
                .tipo("silla")
                .precioBase(15000.0)
                .stock(10)
                .tamano(TamanoMueble.MEDIANO)
                .material("metal")
                .build();
        
        // When: creamos el mueble
        MuebleResponse response = catalogoService.crearMueble(request);
        
        // Then: debe tener ID y datos correctos
        assertNotNull(response.getId());
        assertEquals("Silla Nueva", response.getNombre());
        assertEquals(15000.0, response.getPrecioBase());
        assertEquals(EstadoMueble.ACTIVO, response.getEstado());
    }
    
    @Test
    @DisplayName("Listar todos debe devolver todos los muebles")
    void listarTodos() {
        // Given: crear varios muebles
        crearMuebleEjemplo("Mesa", "mesa");
        crearMuebleEjemplo("Silla", "silla");
        crearMuebleEjemplo("Estante", "estante");
        
        // When: listamos todos
        List<MuebleResponse> muebles = catalogoService.listarTodos();
        
        // Then: debe haber al menos 3 (pueden haber mas de data.sql)
        assertTrue(muebles.size() >= 3);
    }
    
    @Test
    @DisplayName("Obtener por ID debe devolver el mueble correcto")
    void obtenerPorId() {
        // Given: un mueble creado
        MuebleResponse creado = crearMuebleEjemplo("Sillon", "sillon");
        
        // When: lo buscamos por ID
        MuebleResponse encontrado = catalogoService.obtenerPorId(creado.getId());
        
        // Then: debe ser el mismo
        assertEquals(creado.getId(), encontrado.getId());
        assertEquals("Sillon", encontrado.getNombre());
    }
    
    @Test
    @DisplayName("Obtener por ID inexistente debe lanzar excepcion")
    void obtenerPorIdInexistente() {
        // When & Then: buscar un ID que no existe debe lanzar excepcion
        assertThrows(RecursoNoEncontradoException.class, 
                () -> catalogoService.obtenerPorId(99999L));
    }
    
    @Test
    @DisplayName("Actualizar mueble debe modificar los datos")
    void actualizarMueble() {
        // Given: un mueble existente
        MuebleResponse original = crearMuebleEjemplo("Mesa Vieja", "mesa");
        
        // When: lo actualizamos
        MuebleRequest actualizacion = MuebleRequest.builder()
                .nombre("Mesa Nueva")
                .tipo("mesa")
                .precioBase(50000.0)
                .stock(15)
                .tamano(TamanoMueble.GRANDE)
                .material("vidrio")
                .build();
        
        MuebleResponse actualizado = catalogoService.actualizarMueble(original.getId(), actualizacion);
        
        // Then: debe tener los nuevos datos
        assertEquals("Mesa Nueva", actualizado.getNombre());
        assertEquals(50000.0, actualizado.getPrecioBase());
        assertEquals("vidrio", actualizado.getMaterial());
    }
    
    @Test
    @DisplayName("Desactivar mueble debe cambiar estado a INACTIVO")
    void desactivarMueble() {
        // Given: un mueble activo
        MuebleResponse mueble = crearMuebleEjemplo("Cajon", "cajon");
        assertEquals(EstadoMueble.ACTIVO, mueble.getEstado());
        
        // When: lo desactivamos
        catalogoService.desactivarMueble(mueble.getId());
        
        // Then: debe estar inactivo
        MuebleResponse desactivado = catalogoService.obtenerPorId(mueble.getId());
        assertEquals(EstadoMueble.INACTIVO, desactivado.getEstado());
    }
    
    @Test
    @DisplayName("Activar mueble debe cambiar estado a ACTIVO")
    void activarMueble() {
        // Given: un mueble desactivado
        MuebleResponse mueble = crearMuebleEjemplo("Estante", "estante");
        catalogoService.desactivarMueble(mueble.getId());
        
        // When: lo activamos
        catalogoService.activarMueble(mueble.getId());
        
        // Then: debe estar activo
        MuebleResponse activado = catalogoService.obtenerPorId(mueble.getId());
        assertEquals(EstadoMueble.ACTIVO, activado.getEstado());
    }
    
    @Test
    @DisplayName("Listar activos solo debe mostrar muebles activos")
    void listarActivos() {
        // Given: crear muebles y desactivar algunos
        MuebleResponse activo1 = crearMuebleEjemplo("Activo 1", "silla");
        MuebleResponse activo2 = crearMuebleEjemplo("Activo 2", "mesa");
        MuebleResponse inactivo = crearMuebleEjemplo("Inactivo", "estante");
        catalogoService.desactivarMueble(inactivo.getId());
        
        // When: listamos solo activos
        List<MuebleResponse> activos = catalogoService.listarActivos();
        
        // Then: el inactivo no debe estar en la lista
        assertTrue(activos.stream().anyMatch(m -> m.getId().equals(activo1.getId())));
        assertTrue(activos.stream().anyMatch(m -> m.getId().equals(activo2.getId())));
        assertFalse(activos.stream().anyMatch(m -> m.getId().equals(inactivo.getId())));
    }
    
    // Metodo auxiliar para crear muebles de prueba
    private MuebleResponse crearMuebleEjemplo(String nombre, String tipo) {
        MuebleRequest request = MuebleRequest.builder()
                .nombre(nombre)
                .tipo(tipo)
                .precioBase(10000.0)
                .stock(10)
                .tamano(TamanoMueble.MEDIANO)
                .material("madera")
                .build();
        return catalogoService.crearMueble(request);
    }
}