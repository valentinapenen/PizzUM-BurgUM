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

    // Perfil administrador (similar a cliente, pero sólo editar datos y domicilio, no agregar)
    @GetMapping("/perfil")
    public String verPerfilAdmin(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null || !(usuario instanceof Administrador admin)) {
            return "redirect:/iniciar-sesion";
        }
        model.addAttribute("administrador", admin);
        return "administrador/perfil";
    }

    @GetMapping("/perfil/editar")
    public String editarPerfilAdminForm(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null || !(usuario instanceof Administrador admin)) {
            return "redirect:/iniciar-sesion";
        }
        model.addAttribute("administrador", admin);
        return "administrador/perfil-form";
    }

    @PostMapping("/perfil/editar")
    public String editarPerfilAdminSubmit(
            HttpSession session,
            @ModelAttribute("administrador") Administrador formAdmin,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null || !(usuario instanceof Administrador admin)) {
            return "redirect:/iniciar-sesion";
        }

        try {
            // Sólo permite actualizar campos habilitados; el service ya valida que no se cambien cedula/correo
            formAdmin.setCedula(admin.getCedula());
            formAdmin.setCorreo(admin.getCorreo());
            Administrador actualizado = administradorService.actualizarDatosAdmin(admin.getCedula(), formAdmin);
            // refrescar en sesión
            session.setAttribute("usuarioLogueado", actualizado);
            redirectAttributes.addFlashAttribute("exito", "Perfil actualizado correctamente");
            return "redirect:/administrador/perfil";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "administrador/perfil-form";
        }
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