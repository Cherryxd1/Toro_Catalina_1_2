package com.muebleria.app.dto;

import com.muebleria.app.domain.TamanoMueble;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MuebleRequest {
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @NotBlank(message = "El tipo es obligatorio")
    private String tipo;
    
    @NotNull(message = "El precio base es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private Double precioBase;
    
    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;
    
    @NotNull(message = "El tamano es obligatorio")
    private TamanoMueble tamano;
    
    @NotBlank(message = "El material es obligatorio")
    private String material;
}