-- Datos de ejemplo para probar el sistema

-- Insertar muebles de ejemplo
INSERT INTO muebles (nombre, tipo, precio_base, stock, estado, tamano, material) VALUES
('Silla Moderna', 'silla', 25000.00, 15, 'ACTIVO', 'MEDIANO', 'madera'),
('Mesa de Comedor', 'mesa', 120000.00, 8, 'ACTIVO', 'GRANDE', 'madera'),
('Estante Minimalista', 'estante', 85000.00, 10, 'ACTIVO', 'GRANDE', 'metal'),
('Sillon Ejecutivo', 'sillon', 150000.00, 5, 'ACTIVO', 'GRANDE', 'cuero'),
('Cajon Pequeno', 'cajon', 35000.00, 20, 'ACTIVO', 'PEQUENO', 'plastico');

-- Insertar variantes de ejemplo
INSERT INTO variantes (nombre, descripcion, porcentaje_incremento, monto_fijo, es_normal) VALUES
('NORMAL', 'Sin variante especial', 0.0, 0.0, true),
('BARNIZ_PREMIUM', 'Barniz premium de alta calidad', 15.0, 0.0, false),
('COJINES_SEDA', 'Cojines de seda suave y elegante', 25.0, 5000.0, false),
('RUEDAS', 'Ruedas giratorias de alta resistencia', 10.0, 3000.0, false);