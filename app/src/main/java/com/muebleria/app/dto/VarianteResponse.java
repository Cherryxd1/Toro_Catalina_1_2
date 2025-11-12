package com.muebleria.app.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VarianteResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double porcentajeIncremento;
    private Double montoFijo;
    private Boolean esNormal;
}