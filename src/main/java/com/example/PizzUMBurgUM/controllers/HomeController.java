package com.example.PizzUMBurgUM.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String mostrarBienvenida() {
        return "inicio/bienvenida";  // src/main/resources/templates/inicio/bienvenida.html
    }
}
