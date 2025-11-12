package com.muebleria.app.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cotizaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCotizacion estado = EstadoCotizacion.PENDIENTE;

    @OneToMany(mappedBy = "cotizacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CotizacionItem> items = new ArrayList<>();

    @Column(nullable = false)
    private Double total = 0.0;

    private String clienteNombre;

    // Metodo para agregar un item a la cotizacion
    public void agregarItem(CotizacionItem item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        item.setCotizacion(this);
    }

    // Metodo para calcular el total
    public void calcularTotal() {
        if (items == null || items.isEmpty()) {
            this.total = 0.0;
            return;
        }
        this.total = items.stream()
                .mapToDouble(CotizacionItem::getSubtotal)
                .sum();
    }

    // Metodo para confirmar la cotizacion como venta
    public void confirmar() {
        this.estado = EstadoCotizacion.CONFIRMADA;
    }
}