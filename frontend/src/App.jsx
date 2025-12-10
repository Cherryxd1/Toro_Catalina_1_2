// frontend/src/App.jsx
import { useEffect, useState } from 'react';
import { muebleService, cotizacionService } from './services/api';
import './App.css';

function App() {
    const [muebles, setMuebles] = useState([]);
    const [carrito, setCarrito] = useState(null); // Estado para la cotización actual
    const [cliente, setCliente] = useState('Cliente Web'); // Nombre por defecto
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(true);
    const [procesando, setProcesando] = useState(false);

    // Cargar catálogo al iniciar
    useEffect(() => {
        cargarMuebles();
    }, []);

    const cargarMuebles = async () => {
        try {
            setLoading(true);
            const respuesta = await muebleService.listarActivos();
            setMuebles(respuesta.data);
        } catch (err) {
            console.error(err);
            setError('Error conectando con el servidor Backend (Puerto 3036).');
        } finally {
            setLoading(false);
        }
    };

    // Función principal: Agregar al Carrito
    const agregarAlCarrito = async (mueble) => {
        setProcesando(true);
        try {
            let cotizacionActual = carrito;

            // 1. Si no existe cotización, crear una nueva
            if (!cotizacionActual) {
                const resp = await cotizacionService.crear(cliente);
                cotizacionActual = resp.data;
            }

            // 2. Agregar el item a la cotización en el Backend
            // Nota: Enviamos null en varianteId para usar la estrategia "PrecioNormalStrategy"
            const respItem = await cotizacionService.agregarItem(
                cotizacionActual.id,
                mueble.id,
                1 // Cantidad fija de 1 por clic
            );

            // 3. Actualizar el estado local con la respuesta del backend
            setCarrito(respItem.data);
            alert(`¡${mueble.nombre} agregado al carrito!`);

        } catch (err) {
            console.error(err);
            alert("Error al agregar item: " + (err.response?.data?.mensaje || err.message));
        } finally {
            setProcesando(false);
        }
    };

    // Función para Confirmar Venta (Reducir Stock)
    const confirmarCompra = async () => {
        if (!carrito) return;

        if (!window.confirm(`¿Confirmar compra por $${carrito.total}?`)) return;

        setProcesando(true);
        try {
            // Llama al endpoint /confirmar que verifica stock y lo reduce
            await cotizacionService.confirmar(carrito.id);
            alert("¡Compra realizada con éxito! El stock ha sido actualizado.");

            // Reiniciar carrito y recargar muebles para ver el stock actualizado
            setCarrito(null);
            cargarMuebles();
        } catch (err) {
            console.error(err);
            // El backend lanzará StockInsuficienteException si falla
            alert("Error en la compra: " + (err.response?.data?.mensaje || "Stock insuficiente o error interno"));
        } finally {
            setProcesando(false);
        }
    };

    return (
        <div className="container" style={{ maxWidth: '1200px', margin: '0 auto', padding: '2rem', display: 'flex', gap: '2rem' }}>

            {/* SECCIÓN IZQUIERDA: CATÁLOGO */}
            <div style={{ flex: 2 }}>
                <header style={{ marginBottom: '2rem' }}>
                    <h1 style={{ fontSize: '2.5rem', color: '#333' }}> Mueblería Hermanos</h1>
                    <p>Catálogo de Productos</p>
                </header>

                {error && <div style={{ color: 'red', marginBottom: '1rem' }}>{error}</div>}

                {loading ? <p>Cargando...</p> : (
                    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))', gap: '1.5rem' }}>
                        {muebles.map((mueble) => (
                            <div key={mueble.id} style={{
                                border: '1px solid #ddd',
                                borderRadius: '8px',
                                padding: '1rem',
                                opacity: mueble.stock === 0 ? 0.6 : 1
                            }}>
                                <h3>{mueble.nombre}</h3>
                                <p style={{ fontSize: '0.9rem', color: '#666' }}>{mueble.tipo} - {mueble.material}</p>
                                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', margin: '1rem 0' }}>
                                    <span style={{ fontSize: '1.2rem', fontWeight: 'bold', color: '#27ae60' }}>
                                        ${mueble.precioBase}
                                    </span>
                                    <span style={{ fontSize: '0.8rem', background: '#eee', padding: '2px 6px', borderRadius: '4px' }}>
                                        Stock: {mueble.stock}
                                    </span>
                                </div>
                                <button
                                    disabled={mueble.stock === 0 || procesando}
                                    onClick={() => agregarAlCarrito(mueble)}
                                    style={{
                                        width: '100%',
                                        padding: '0.5rem',
                                        background: mueble.stock > 0 ? '#3498db' : '#ccc',
                                        color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer'
                                    }}
                                >
                                    {mueble.stock === 0 ? 'Agotado' : 'Agregar +'}
                                </button>
                            </div>
                        ))}
                    </div>
                )}
            </div>

            {/* SECCIÓN DERECHA: CARRITO DE COMPRAS */}
            <div style={{ flex: 1, borderLeft: '1px solid #eee', paddingLeft: '2rem' }}>
                <h2> Carrito de Compras</h2>
                <div style={{ background: '#f9f9f9', padding: '1rem', borderRadius: '8px', minHeight: '200px' }}>

                    {!carrito || carrito.items.length === 0 ? (
                        <p style={{ color: '#888', fontStyle: 'italic' }}>El carrito está vacío.</p>
                    ) : (
                        <>
                            <div style={{ marginBottom: '1rem' }}>
                                <p><strong>Cliente:</strong> {carrito.clienteNombre}</p>
                                <p><strong>ID Cotización:</strong> {carrito.id}</p>
                            </div>

                            <ul style={{ listStyle: 'none', padding: 0 }}>
                                {carrito.items.map((item) => (
                                    <li key={item.id} style={{
                                        display: 'flex',
                                        justifyContent: 'space-between',
                                        borderBottom: '1px solid #ddd',
                                        padding: '0.5rem 0'
                                    }}>
                                        <div>
                                            <strong>{item.muebleNombre}</strong>
                                            <div style={{ fontSize: '0.8rem', color: '#666' }}>Cant: {item.cantidad}</div>
                                        </div>
                                        <div style={{ fontWeight: 'bold' }}>
                                            ${item.subtotal}
                                        </div>
                                    </li>
                                ))}
                            </ul>

                            <div style={{ marginTop: '2rem', borderTop: '2px solid #333', paddingTop: '1rem' }}>
                                <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '1.2rem', marginBottom: '1rem' }}>
                                    <span>Total:</span>
                                    <strong>${carrito.total}</strong>
                                </div>
                                <button
                                    onClick={confirmarCompra}
                                    disabled={procesando}
                                    style={{
                                        width: '100%',
                                        padding: '1rem',
                                        background: '#27ae60',
                                        color: 'white',
                                        border: 'none',
                                        borderRadius: '6px',
                                        fontWeight: 'bold',
                                        cursor: 'pointer',
                                        fontSize: '1rem'
                                    }}
                                >
                                    {procesando ? 'Procesando...' : 'Confirmar Compra'}
                                </button>
                            </div>
                        </>
                    )}
                </div>
            </div>
        </div>
    );
}

export default App;