package com.muebleria.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MuebleriaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MuebleriaApplication.class, args);
        System.out.println("=================================");
        System.out.println("Muebleria Los Muebles Hermanos");
        System.out.println("Sistema iniciado correctamente!");
        System.out.println("=================================");
    }
}