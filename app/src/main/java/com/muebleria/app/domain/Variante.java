package com.muebleria.app.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "variantes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Variante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre de la variante es obligatorio")
    @Column(nullable = false, unique = true)
    private String nombre; // BARNIZ_PREMIUM, COJINES_SEDA, RUEDAS
    
    @NotBlank(message = "La descripcion es obligatoria")
    @Column(nullable = false)
    private String descripcion;
    
    @NotNull(message = "El porcentaje de incremento es obligatorio")
    @DecimalMin(value = "0.0", message = "El porcentaje no puede ser negativo")
    @Column(nullable = false)
    private Double porcentajeIncremento; // Ej: 15.0 significa +15%
    
    @Column(nullable = false)
    private Double montoFijo = 0.0; // Alternativa: incremento en pesos
    
    @Column(nullable = false)
    private Boolean esNormal = false; // true si es la variante "NORMAL" (sin cambio de precio)
}