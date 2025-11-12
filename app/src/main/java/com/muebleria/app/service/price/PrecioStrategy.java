package com.muebleria.app.service.price;

import com.muebleria.app.domain.Mueble;
import com.muebleria.app.domain.Variante;

public interface PrecioStrategy {
    Double calcularPrecio(Mueble mueble, Variante variante);
}