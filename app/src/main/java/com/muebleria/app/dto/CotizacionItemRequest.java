package com.muebleria.app.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CotizacionItemRequest {

    @NotNull(message = "El ID del mueble es obligatorio")
    private Long muebleId;

    private Long varianteId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;
}