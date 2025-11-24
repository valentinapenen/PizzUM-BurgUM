package com.example.PizzUMBurgUM.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        // Opcional: loguear el error
        System.out.println("Error atrapado: " + e.getMessage());
        // Redirige al login si algo falla
        return "redirect:/iniciar-sesion";
    }
}
