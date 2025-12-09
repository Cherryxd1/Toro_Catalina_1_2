
import { useEffect, useState } from 'react';
import api from './services/api';
import './App.css';

function App() {
    const [muebles, setMuebles] = useState([]);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(true);

    // Cargar muebles al iniciar
    useEffect(() => {
        cargarMuebles();
    }, []);

    const cargarMuebles = async () => {
        try {
            setLoading(true);
            // Conecta con tu MuebleController.java del backend
            const respuesta = await api.get('/muebles/activos');
            setMuebles(respuesta.data);
            setError('');
        } catch (err) {
            console.error("Error conectando:", err);
            setError('Error al conectar con el servidor. Asegúrate de que el backend (puerto 3036/8080) esté corriendo.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="container" style={{ maxWidth: '1200px', margin: '0 auto', padding: '2rem' }}>
            <header style={{ marginBottom: '2rem', textAlign: 'center' }}>
                <h1 style={{ fontSize: '2.5rem', color: '#333' }}>🛋️ Mueblería Los Muebles Hermanos</h1>
                <p>Catálogo de Productos Disponibles</p>
            </header>

            {error && (
                <div style={{ backgroundColor: '#ffebee', color: '#c62828', padding: '1rem', borderRadius: '4px', marginBottom: '1rem' }}>
                    {error}
                </div>
            )}

            {loading ? (
                <p style={{ textAlign: 'center' }}>Cargando catálogo...</p>
            ) : (
                <div style={{
                    display: 'grid',
                    gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))',
                    gap: '2rem'
                }}>
                    {muebles.length > 0 ? (
                        muebles.map((mueble) => (
                            <div key={mueble.id} style={{
                                border: '1px solid #e0e0e0',
                                borderRadius: '8px',
                                padding: '1.5rem',
                                backgroundColor: 'white',
                                boxShadow: '0 2px 4px rgba(0,0,0,0.05)'
                            }}>
                                <h3 style={{ margin: '0 0 0.5rem 0', color: '#2c3e50' }}>{mueble.nombre}</h3>
                                <div style={{ fontSize: '0.9rem', color: '#666', marginBottom: '1rem' }}>
                                    <p style={{ margin: '5px 0' }}>🏷️ Tipo: {mueble.tipo}</p>
                                    <p style={{ margin: '5px 0' }}>🪵 Material: {mueble.material}</p>
                                    <p style={{ margin: '5px 0' }}>📏 Tamaño: {mueble.tamano}</p>
                                </div>

                                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: '1rem' }}>
                                    <span style={{ fontSize: '1.5rem', fontWeight: 'bold', color: '#27ae60' }}>
                                        ${mueble.precioBase}
                                    </span>
                                    <span style={{
                                        padding: '4px 8px',
                                        borderRadius: '4px',
                                        backgroundColor: mueble.stock > 0 ? '#e8f5e9' : '#ffebee',
                                        color: mueble.stock > 0 ? '#2e7d32' : '#c62828',
                                        fontSize: '0.85rem'
                                    }}>
                                        Stock: {mueble.stock}
                                    </span>
                                </div>

                                <button
                                    disabled={mueble.stock === 0}
                                    style={{
                                        width: '100%',
                                        marginTop: '1.5rem',
                                        padding: '0.8rem',
                                        backgroundColor: mueble.stock > 0 ? '#3498db' : '#bdc3c7',
                                        color: 'white',
                                        border: 'none',
                                        borderRadius: '4px',
                                        cursor: mueble.stock > 0 ? 'pointer' : 'not-allowed',
                                        fontWeight: 'bold'
                                    }}
                                    onClick={() => alert(`Has seleccionado: ${mueble.nombre}`)}
                                >
                                    {mueble.stock > 0 ? 'Agregar al Carrito' : 'Agotado'}
                                </button>
                            </div>
                        ))
                    ) : (
                        <p style={{ gridColumn: '1/-1', textAlign: 'center' }}>No hay muebles activos para mostrar.</p>
                    )}
                </div>
            )}
        </div>
    );
}

export default App;