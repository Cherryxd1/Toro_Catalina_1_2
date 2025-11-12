# Sistema de Gestión de Mueblería - Los Muebles Hermanos S.A

## Descripción

Sistema backend desarrollado con Spring Boot para la gestión de inventario, cotizaciones y ventas de una mueblería. Permite administrar muebles, aplicar variantes que modifican precios, crear cotizaciones y procesar ventas con control automático de stock.

## Tecnologías Utilizadas

- Java 17
- Spring Boot 3.1.5
- Spring Data JPA
- MySQL 8.0
- Maven
- JUnit 5
- Lombok

## Requisitos Previos

- JDK 17 o superior
- MySQL 8.0 o superior
- Maven 3.6 o superior

## Configuración

### 1. Clonar el Repositorio
```bash
git clone https://github.com/tu-usuario/muebleria-spring-boot.git
cd muebleria-spring-boot
```

### 2. Configurar MySQL

Edita `src/main/resources/application.properties` con tus credenciales:
```properties
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

La base de datos se crea automáticamente al ejecutar la aplicación.

### 3. Compilar y Ejecutar
```bash
mvn clean install
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

## Estructura del Proyecto
```
src/
├── main/
│   ├── java/com/muebleria/app/
│   │   ├── controller/       # API REST
│   │   ├── domain/           # Entidades
│   │   ├── service/          # Lógica de negocio
│   │   ├── repository/       # Acceso a datos
│   │   ├── factory/          # Patrón Factory
│   │   └── exception/        # Manejo de errores
│   └── resources/
│       ├── application.properties
│       └── data.sql          # Datos de ejemplo
└── test/
    └── java/                 # Pruebas JUnit
```

## Patrones de Diseño

### Strategy
Implementado en `service/price/` para calcular precios con o sin variantes.

### Factory
Implementado en `factory/VarianteFactory.java` para crear variantes predefinidas.

## Endpoints Principales

### Muebles
- `POST /api/muebles` - Crear mueble
- `GET /api/muebles` - Listar todos
- `GET /api/muebles/activos` - Listar activos
- `PUT /api/muebles/{id}` - Actualizar
- `PATCH /api/muebles/{id}/desactivar` - Desactivar

### Variantes
- `POST /api/variantes` - Crear variante
- `POST /api/variantes/tipo/{tipo}` - Crear variante predefinida
- `GET /api/variantes` - Listar todas

### Cotizaciones
- `POST /api/cotizaciones` - Crear cotización
- `POST /api/cotizaciones/{id}/items` - Agregar item
- `POST /api/cotizaciones/{id}/confirmar` - Confirmar venta

## Ejecutar Pruebas
```bash
mvn test
```

Total de pruebas: 15
- CRUD de muebles: 8 pruebas
- Cálculo de precios: 4 pruebas
- Control de stock: 3 pruebas

## Ejemplos de Uso

### Crear un Mueble
```bash
curl -X POST http://localhost:8080/api/muebles \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Silla Moderna",
    "tipo": "silla",
    "precioBase": 25000,
    "stock": 15,
    "tamano": "MEDIANO",
    "material": "madera"
  }'
```

### Crear Variante Predefinida
```bash
curl -X POST http://localhost:8080/api/variantes/tipo/BARNIZ_PREMIUM
```

### Crear y Confirmar Cotización
```bash
# 1. Crear cotización
curl -X POST http://localhost:8080/api/cotizaciones \
  -H "Content-Type: application/json" \
  -d '{"clienteNombre": "Juan Pérez"}'

# 2. Agregar item (usar ID de cotización del paso anterior)
curl -X POST http://localhost:8080/api/cotizaciones/1/items \
  -H "Content-Type: application/json" \
  -d '{
    "muebleId": 1,
    "varianteId": 2,
    "cantidad": 3
  }'

# 3. Confirmar venta
curl -X POST http://localhost:8080/api/cotizaciones/1/confirmar
```

## Autora

Catalina Toro Plaza 

Evaluación 2 - Ingeniería de Software  
Universidad del Bío-Bío  
11 de Noviembre del 2025