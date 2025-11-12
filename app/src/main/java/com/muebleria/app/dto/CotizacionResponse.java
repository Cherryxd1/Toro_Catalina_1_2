package com.muebleria.app.dto;

import com.muebleria.app.domain.EstadoCotizacion;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CotizacionResponse {
    private Long id;
    private LocalDateTime fecha;
    private EstadoCotizacion estado;
    private String clienteNombre;
    private Double total;
    private List<CotizacionItemResponse> items;
}