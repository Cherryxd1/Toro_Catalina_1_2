// src/App.jsx
import { useEffect, useState } from 'react';
import api from './services/api';
import './App.css'; // Usaremos los estilos básicos que ya trae Vite

function App() {
    const [muebles, setMuebles] = useState([]);
    const [error, setError] = useState('');

    // Esta función se ejecuta automáticamente al cargar la página
    useEffect(() => {
        cargarMuebles();
    }, []);

    const cargarMuebles = async () => {
        try {
            // 1. Pedimos los datos al Backend (Java)
            const respuesta = await api.get('/muebles/activos');
            // 2. Guardamos los datos en el estado de React
            setMuebles(respuesta.data);
        } catch (err) {
            console.error("Error conectando:", err);
            setError('No se pudo conectar con el servidor. ¿Está corriendo el Backend?');
        }
    };

    return (
        <div className="container" style={{ padding: '2rem' }}>
            <h1>🛋️ Mueblería Los Muebles Hermanos</h1>

            {error && <p style={{ color: 'red' }}>{error}</p>}

            <h2>Catálogo Disponible</h2>

            {muebles.length === 0 && !error ? (
                <p>Cargando muebles...</p>
            ) : (
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(250px, 1fr))', gap: '20px' }}>
                    {muebles.map((mueble) => (
                        <div key={mueble.id} style={{ border: '1px solid #ccc', borderRadius: '8px', padding: '15px', textAlign: 'left' }}>
                            <h3>{mueble.nombre}</h3>
                            <p><strong>Tipo:</strong> {mueble.tipo}</p>
                            <p><strong>Material:</strong> {mueble.material}</p>
                            <p><strong>Tamaño:</strong> {mueble.tamano}</p>
                            <h4 style={{ color: '#2ecc71' }}>${mueble.precioBase}</h4>
                            <p style={{ fontSize: '0.9em', color: '#666' }}>Stock: {mueble.stock}</p>

                            <button disabled={mueble.stock === 0} style={{ width: '100%', marginTop: '10px' }}>
                                {mueble.stock > 0 ? 'Agregar al Carrito' : 'Agotado'}
                            </button>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

export default App;