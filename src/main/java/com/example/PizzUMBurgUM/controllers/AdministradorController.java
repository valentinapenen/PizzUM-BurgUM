package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.entities.Administrador;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/administrador")
@SessionAttributes("usuarioLogueado")
public class AdministradorController {

    @GetMapping("/home")
    public String mostrarInicioAdministrador(@SessionAttribute("usuarioLogueado") Administrador administrador, Model model){
        model.addAttribute("administrador", administrador);

        return "paginaInicioAdministrador";
    }
}
