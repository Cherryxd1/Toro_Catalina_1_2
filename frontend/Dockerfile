# ==========================================
# DOCKERFILE PARA FRONTEND (REACT + VITE)
# ==========================================


# Etapa 1: Construcción (Build)
FROM node:18-alpine AS build

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos de dependencias
COPY package*.json ./

# Instalar dependencias
RUN npm install

# Copiar todo el código fuente
COPY . .

# Compilar la aplicación para producción
RUN npm run build

# ==========================================
# Etapa 2: Servidor Web (Nginx)
# ==========================================
FROM nginx:alpine

# Copiar los archivos compilados desde la etapa anterior
COPY --from=build /app/dist /usr/share/nginx/html

# Configurar Nginx para React Router (SPA)
# Esto asegura que todas las rutas se redirijan a index.html
RUN echo 'server { \
    listen 80; \
    server_name localhost; \
    location / { \
        root /usr/share/nginx/html; \
        index index.html index.htm; \
        try_files $uri $uri/ /index.html; \
    } \
    location /api { \
        proxy_pass http://backend:8080; \
        proxy_http_version 1.1; \
        proxy_set_header Upgrade $http_upgrade; \
        proxy_set_header Connection "upgrade"; \
        proxy_set_header Host $host; \
        proxy_cache_bypass $http_upgrade; \
    } \
}' > /etc/nginx/conf.d/default.conf

# Exponer el puerto 80
EXPOSE 80

# Comando para iniciar Nginx
CMD ["nginx", "-g", "daemon off;"]