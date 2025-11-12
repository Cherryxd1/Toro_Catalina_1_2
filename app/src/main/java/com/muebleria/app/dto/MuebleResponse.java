package com.muebleria.app.dto;

import com.muebleria.app.domain.EstadoMueble;
import com.muebleria.app.domain.TamanoMueble;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MuebleResponse {
    private Long id;
    private String nombre;
    private String tipo;
    private Double precioBase;
    private Integer stock;
    private EstadoMueble estado;
    private TamanoMueble tamano;
    private String material;
}