// frontend/src/services/api.js
import axios from 'axios';

// Asegúrate que el puerto coincida con tu backend (3036 según tu application.properties)
const API_URL = 'http://localhost:3036/api';

const api = axios.create({
    baseURL: API_URL,
    headers: {
        'Content-Type': 'application/json'
    }
});

export const muebleService = {
    listarActivos: () => api.get('/muebles/activos'),
};

export const cotizacionService = {
    // 1. Crear una nueva cotización (carrito)
    crear: (clienteNombre) => api.post('/cotizaciones', { clienteNombre }),

    // 2. Agregar item al carrito
    agregarItem: (cotizacionId, muebleId, cantidad = 1) =>
        api.post(`/cotizaciones/${cotizacionId}/items`, {
            muebleId: muebleId,
            varianteId: null, // Por defecto null (NORMAL) según tu lógica backend
            cantidad: cantidad
        }),

    // 3. Confirmar la compra (esto reduce el stock en la BD)
    confirmar: (cotizacionId) => api.post(`/cotizaciones/${cotizacionId}/confirmar`),

    // Obtener detalles del carrito actual
    obtener: (id) => api.get(`/cotizaciones/${id}`)
};

export default api;