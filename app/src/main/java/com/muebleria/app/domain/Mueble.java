package com.muebleria.app.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "muebles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mueble {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre del mueble es obligatorio")
    @Column(nullable = false)
    private String nombre;
    
    @NotBlank(message = "El tipo de mueble es obligatorio")
    @Column(nullable = false)
    private String tipo; // silla, sillon, mesa, estante, cajon
    
    @NotNull(message = "El precio base es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    @Column(nullable = false)
    private Double precioBase;
    
    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private Integer stock;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoMueble estado = EstadoMueble.ACTIVO;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TamanoMueble tamano;
    
    @NotBlank(message = "El material es obligatorio")
    @Column(nullable = false)
    private String material; // madera, metal, plastico, etc
    
    // Metodo para desactivar el mueble (no eliminarlo)
    public void desactivar() {
        this.estado = EstadoMueble.INACTIVO;
    }
    
    // Metodo para activar el mueble
    public void activar() {
        this.estado = EstadoMueble.ACTIVO;
    }
    
    // Metodo para verificar si hay stock disponible
    public boolean tieneStock(Integer cantidad) {
        return this.stock >= cantidad;
    }
    
    // Metodo para reducir el stock
    public void reducirStock(Integer cantidad) {
        if (!tieneStock(cantidad)) {
            throw new IllegalArgumentException("Stock insuficiente");
        }
        this.stock -= cantidad;
    }
}