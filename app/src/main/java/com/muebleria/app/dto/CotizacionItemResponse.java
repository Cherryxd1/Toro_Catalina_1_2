package com.muebleria.app.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CotizacionItemResponse {
    private Long id;
    private String muebleNombre;
    private String varianteNombre;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
}