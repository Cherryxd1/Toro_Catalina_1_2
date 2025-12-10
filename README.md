# Sistema de Gestión de Mueblería - Los Muebles Hermanos S.A
Evaluación 3 - Ingeniería de Software 
Estudiante: Catalina Toro Plaza  
Fecha: Diciembre 2024

---

## 📖 Descripción del Proyecto

Sistema web completo dockerizado para la gestión de inventario, cotizaciones y ventas
de una mueblería. Implementa arquitectura en 3 capas (Frontend, Backend, Base de Datos)
con control automático de stock y aplicación de patrones de diseño.

---

## 🛠️ Tecnologías Utilizadas

### Backend
- **Java 17** con Spring Boot 3.1.5
- **Spring Data JPA** para persistencia
- **MySQL 8.0** como base de datos
- **Lombok** para reducir código boilerplate
- **JUnit 5** para testing (15 pruebas unitarias)

### Frontend
- **React 19** con Vite
- **Axios** para consumo de API REST
- **CSS3** para estilos

### DevOps
- **Docker** para contenedorización
- **Docker Compose** para orquestación
- **Nginx** como servidor web (frontend)

---

## Arquitectura del Sistema
```
┌─────────────────┐      HTTP/REST     ┌──────────────────┐
│   FRONTEND      │ ◄─────────────────► │    BACKEND       │
│  (React+Nginx)  │   localhost:8080    │  (Spring Boot)   │
│   Puerto 80     │                     │   Puerto 8080    │
└─────────────────┘                     └──────────────────┘
                                               │
                                               │ JDBC
                                               ▼
                                        ┌──────────────────┐
                                        │     MYSQL        │
                                        │  Puerto 3307     │
                                        └──────────────────┘
```

---

##  Instalación y Ejecución

### Requisitos Previos
- Docker Desktop instalado y corriendo
- Puerto 80, 8080 y 3307 disponibles

### Pasos para Ejecutar
```bash
# 1. Clonar el repositorio
git clone [TU_URL_DE_GITHUB]
cd Toro_Catalina_1_2

# 2. Levantar todos los servicios
docker-compose up -d --build

# 3. Verificar que los contenedores estén corriendo
docker ps

# Deberías ver:
# - muebleria_mysql (puerto 3307)
# - muebleria_backend (puerto 8080)
# - muebleria_frontend (puerto 80)

# 4. Acceder a la aplicación
# Frontend: http://localhost
# API REST: http://localhost:8080/api/muebles/activos
```

### Detener el Sistema
```bash
docker-compose down

# Para eliminar también los datos de la base de datos:
docker-compose down -v
```

---

##  Testing

El proyecto incluye 15 pruebas unitarias que validan:

### 1. Gestión de Catálogo (8 pruebas)
- ✅ Crear mueble
- ✅ Listar todos los muebles
- ✅ Obtener por ID
- ✅ Actualizar mueble
- ✅ Desactivar/Activar mueble
- ✅ Listar solo activos
- ✅ Manejo de errores (ID inexistente)

### 2. Cálculo de Precios con Variantes (4 pruebas)
- ✅ Precio sin variante (normal)
- ✅ Precio con incremento porcentual
- ✅ Precio con monto fijo
- ✅ Precio con combinación (porcentaje + monto fijo)

### 3. Control de Stock (3 pruebas)
- ✅ Venta exitosa con stock suficiente
- ✅ Venta rechazada por stock insuficiente
- ✅ Venta con stock exacto

**Ejecutar los tests:**
```bash
cd app
./mvnw test
```

---

##  Patrones de Diseño Implementados

### 1. **Strategy Pattern**
**Ubicación:** `app/src/main/java/com/muebleria/app/service/price/`

**Problema:** Calcular precios de muebles con diferentes estrategias (normal vs con variante).

**Solución:**
- Interfaz: `PrecioStrategy.java`
- Estrategia Normal: `PrecioNormalStrategy.java` (precio base sin modificaciones)
- Estrategia con Variante: `PrecioConVarianteStrategy.java` (aplica porcentaje + monto fijo)

**Beneficio:** Permite cambiar el algoritmo de cálculo dinámicamente sin modificar
el código del servicio principal (`PrecioService.java`).

###  **Factory Pattern**
**Ubicación:** `app/src/main/java/com/muebleria/app/factory/VarianteFactory.java`

**Problema:** Crear objetos complejos de tipo `Variante` con configuraciones predefinidas.

**Solución:**
```java
public Variante crearVariante(String tipo) {
    switch (tipo.toUpperCase()) {
        case "BARNIZ_PREMIUM":
            return Variante.builder()
                .nombre("BARNIZ_PREMIUM")
                .porcentajeIncremento(15.0)
                .build();
        // ... otros casos
    }
}
```

**Beneficio:** Centraliza la lógica de creación, evita código duplicado y facilita
agregar nuevas variantes.

---

## Dockerización

### Dockerfile del Backend (Multi-Stage Build)
```dockerfile
# Etapa 1: Compilación
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Ventajas:**
- Imagen final 60% más liviana (solo JRE, no JDK)
- Compilación aislada en la primera etapa
- Optimización de capas de Docker para mejor cache

### Dockerfile del Frontend
```dockerfile
# Etapa 1: Build
FROM node:18-alpine as build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Etapa 2: Servidor Web
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

### Docker Compose (Orquestación)

**Características implementadas:**
-  Red compartida (`muebleria_network`) para comunicación entre contenedores
-  Volumen persistente para MySQL (los datos sobreviven a reinicios)
-  Health checks para garantizar que MySQL esté listo antes de iniciar el backend
-  Dependencias ordenadas: `mysql → backend → frontend`

---

## Endpoints de la API

### Muebles
```http
POST   /api/muebles              # Crear mueble
GET    /api/muebles              # Listar todos
GET    /api/muebles/activos      # Listar solo activos
GET    /api/muebles/{id}         # Obtener por ID
PUT    /api/muebles/{id}         # Actualizar
PATCH  /api/muebles/{id}/desactivar  # Desactivar
```

### Cotizaciones
```http
POST   /api/cotizaciones                    # Crear cotización
POST   /api/cotizaciones/{id}/items         # Agregar item
POST   /api/cotizaciones/{id}/confirmar     # Confirmar venta
GET    /api/cotizaciones/{id}               # Obtener detalles
```

### Variantes
```http
POST   /api/variantes                # Crear variante custom
POST   /api/variantes/tipo/{tipo}   # Crear variante predefinida
GET    /api/variantes                # Listar todas
```

---

## Seguridad y Buenas Prácticas

1. **Usuario no-root para MySQL:** La aplicación se conecta con `muebleria_user`,
   no con el usuario `root` administrativo.

2. **Variables de entorno:** Las credenciales no están hardcodeadas en el código.

3. **Health checks:** Garantizan que los servicios estén listos antes de conectarse.

4. **Validaciones en el backend:** Uso de `@Valid` y `@NotNull` en los DTOs.

5. **Manejo de excepciones:** Controlador global con `@RestControllerAdvice`.

---

## Resolución de Problemas

### Problema: "Port 80 is already in use"
**Solución:** Cambia el puerto del frontend en `docker-compose.yml`:
```yaml
frontend:
  ports:
    - "8081:80"  # Accede con http://localhost:8081
```

### Problema: "Connection refused to MySQL"
**Solución:** Espera 30 segundos para que MySQL termine de inicializar.
Verifica con: `docker-compose logs mysql`

### Problema: El frontend no carga
**Solución:**
```bash
docker-compose down
docker-compose up -d --build
```

---

## Dependencias Adicionales

### Backend (pom.xml)
- `spring-boot-devtools`: Recarga automática en desarrollo
- `spring-boot-starter-validation`: Validaciones de entrada
- `lombok`: Reduce código boilerplate

### Frontend (package.json)
- `axios`: Cliente HTTP para consumir la API
- `vite`: Empaquetador moderno y rápido

---

**Catalina Toro Plaza**  
Universidad del Bío-Bío  
Ingeniería Civil en Informática  
Evaluación 3 - Ingeniería de Software

---

Este proyecto es de uso académico para la asignatura de Ingeniería de Software.