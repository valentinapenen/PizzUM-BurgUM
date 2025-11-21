package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.controllers.DTOS.CreacionAdministradorRequest;
import com.example.PizzUMBurgUM.entities.Administrador;
import com.example.PizzUMBurgUM.services.AdministradorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/administrador")
@SessionAttributes("usuarioLogueado")
public class AdministradorController {

    private final AdministradorService administradorService;

    public AdministradorController(AdministradorService administradorService) {
        this.administradorService = administradorService;
    }

    @GetMapping("/home")
    public String mostrarInicioAdministrador(@SessionAttribute("usuarioLogueado") Administrador administrador, Model model){
        model.addAttribute("administrador", administrador);

        return "administrador/inicio-administrador";
    }

    @GetMapping("/crearAdministrador")
    public String mostrarCrearAdministrador(Model model){

        model.addAttribute("creacionAdmin", new CreacionAdministradorRequest());

        return "administrador/crear-administrador";
    }

    @PostMapping("/crearAdministrador")
    public String procesarCreacionAdministrador(@Valid @ModelAttribute("creacionAdmin") CreacionAdministradorRequest creacionAdministradorRequest, Model model, RedirectAttributes redirectAttributes){

        try{
            administradorService.crearAdministrador(creacionAdministradorRequest);
            redirectAttributes.addFlashAttribute("exito", "Creación de administrador exitosa.");
            return "redirect:/administrador";
        }
        catch(IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
            return "administrador/crear-administrador";

        }
    }

}
