package com.muebleria.app.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "cotizacion_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CotizacionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cotizacion_id", nullable = false)
    private Cotizacion cotizacion;

    @ManyToOne
    @JoinColumn(name = "mueble_id", nullable = false)
    private Mueble mueble;

    @ManyToOne
    @JoinColumn(name = "variante_id")
    private Variante variante; // Puede ser null (sin variante = normal)

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Double precioUnitario = 0.0;

    @Column(nullable = false)
    private Double subtotal = 0.0;

    // Metodo para calcular el precio unitario (se llama desde el servicio)
    public void calcularPrecioUnitario(Double precioCalculado) {
        this.precioUnitario = precioCalculado;
        this.subtotal = this.precioUnitario * this.cantidad;
    }
}