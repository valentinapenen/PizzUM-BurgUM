package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.controllers.DTOS.CreacionAdministradorRequest;
import com.example.PizzUMBurgUM.controllers.DTOS.DomicilioRequest;
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

    @GetMapping("/inicio")
    public String mostrarInicioAdministrador(HttpSession session, Model model){

        Usuario usuario = (Usuario)  session.getAttribute("usuarioLogueado");

        if(usuario == null || !(usuario instanceof Administrador)){
            return "redirect:/iniciar-sesion";
        }

        model.addAttribute("administrador", usuario);
        return "administrador/inicio-administrador";
    }

    // Listado de administradores
    @GetMapping("/lista")
    public String listarAdministradores(Model model) {
        model.addAttribute("administradores", administradorService.listarTodos());
        return "administrador/lista";
    }

    @GetMapping("/crearAdministrador")
    public String mostrarCrearAdministrador(HttpSession session, Model model){

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null || !(usuario instanceof Administrador)){
            return "redirect:/iniciar-sesion";
        }

        CreacionAdministradorRequest dto = new CreacionAdministradorRequest();
        dto.setDomicilio_facturacion(new DomicilioRequest());

        model.addAttribute("creacionAdmin", dto);

        return "administrador/form";
    }


    @PostMapping("/crearAdministrador")
    public String procesarCreacionAdministrador(@Valid @ModelAttribute("creacionAdmin") CreacionAdministradorRequest creacionAdministradorRequest, HttpSession session, Model model, RedirectAttributes redirectAttributes){

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null || !(usuario instanceof Administrador)){
            return "redirect:/iniciar-sesion";
        }

        try{
            administradorService.crearAdministrador(creacionAdministradorRequest);
            redirectAttributes.addFlashAttribute("exito", "Creación de administrador exitosa.");
            return "redirect:/administrador/inicio";
        } catch(IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
            return "administrador/form";
        }
    }

}
