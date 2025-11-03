package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.services.UsuarioServicio;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/usuario")

public class UsuarioController {

    private UsuarioServicio usuarioServicio;

    //@GetMapping("/login")
   // public String mostrarLogin(Model model){}


}
