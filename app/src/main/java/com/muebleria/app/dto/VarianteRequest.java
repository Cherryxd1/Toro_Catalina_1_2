package com.muebleria.app.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VarianteRequest {
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @NotBlank(message = "La descripcion es obligatoria")
    private String descripcion;
    
    @NotNull(message = "El porcentaje de incremento es obligatorio")
    @DecimalMin(value = "0.0", message = "El porcentaje no puede ser negativo")
    private Double porcentajeIncremento;
    
    private Double montoFijo = 0.0;
    
    private Boolean esNormal = false;
}