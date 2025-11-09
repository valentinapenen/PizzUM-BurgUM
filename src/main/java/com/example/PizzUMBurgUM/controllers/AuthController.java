package com.example.PizzUMBurgUM.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/iniciar-sesion")
    public String mostrarLogin() {
        // Devuelve el archivo: src/main/resources/templates/auth/iniciar-sesion.html
        return "inicio/iniciar-sesion";
    }

    @GetMapping("/crear-cuenta")
    public String mostrarRegistro() {
        // Devuelve el archivo: src/main/resources/templates/auth/crear-cuenta.html
        return "inicio/crear-cuenta";
    }
}
