package com.muebleria.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                        "http://localhost:3000",  // Tu puerto original
                        "http://localhost:3001",  // <--- AGREGA ESTA LÍNEA (El puerto de tu error)
                        "http://localhost:5173",  // (Opcional) Vite a veces usa este puerto también
                        "http://localhost:80",
                        "http://localhost"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(false);
    }
}