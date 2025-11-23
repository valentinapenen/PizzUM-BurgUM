package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.controllers.DTOS.CreacionAdministradorRequest;
import com.example.PizzUMBurgUM.entities.Administrador;
import com.example.PizzUMBurgUM.entities.Usuario;
import com.example.PizzUMBurgUM.services.AdministradorService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/administrador")
public class AdministradorController {

    private final AdministradorService administradorService;

    public AdministradorController(AdministradorService administradorService) {
        this.administradorService = administradorService;
    }

    @GetMapping("/home")
    public String mostrarInicioAdministrador(HttpSession session, Model model){

        Usuario usuario = (Usuario)  session.getAttribute("usuarioLogueado");

        if(usuario == null || !(usuario instanceof Administrador)){
            return "redirect:/usuario/login";
        }
        Administrador administrador = (Administrador) usuario;
        model.addAttribute("administrador", administrador);
        return "administrador/inicio-administrador";
    }

    @GetMapping("/crearAdministrador")
    public String mostrarCrearAdministrador(HttpSession session, Model model){

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null || !(usuario instanceof Administrador)){
            return "redirect:/usuario/login";
        }

        model.addAttribute("creacionAdmin", new CreacionAdministradorRequest());

        return "administrador/crear-administrador";
    }

    @PostMapping("/crearAdministrador")
    public String procesarCreacionAdministrador(@Valid @ModelAttribute("creacionAdmin") CreacionAdministradorRequest creacionAdmi