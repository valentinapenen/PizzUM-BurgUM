package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.entities.Cliente;
import com.example.PizzUMBurgUM.entities.Domicilio;
import com.example.PizzUMBurgUM.entities.Usuario;
import com.example.PizzUMBurgUM.services.DomicilioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cliente/domicilios")
public class ClienteDomicilioController {

    @Autowired
    private DomicilioService domicilioService;

    // LISTAR DOMICILIOS
    @GetMapping
    public String listar(HttpSession session, Model model, RedirectAttributes redirectAttributes) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null || !(usuario instanceof Cliente cliente)) {
            return "redirect:/usuario/login";
        }

        List<Domicilio> domicilios = domicilioService.listarPorClienteId(cliente.getId());

        model.addAttribute("cliente", cliente);
        model.addAttribute("domicilios", domicilios);

        return "cliente/domicilio/lista";
    }

    // FORM NUEVO
    @GetMapping("/nuevo")
    public String mostrarFormNuevo(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null || !(usuario instanceof Cliente cliente)) {
            return "redirect:/usuario/login";
        }

        model.addAttribute("cliente", cliente);
        model.addAttribute("domicilio", new Domicilio());

        return "cliente/domicilio/form";
    }

    // FORM EDITAR
    @GetMapping("/{id}/editar")
    public String mostrarFormEditar(@PathVariable Long id,
                                    HttpSession session,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null || !(usuario instanceof Cliente cliente)) {
            return "redirect:/usuario/login";
        }

        try {
            Domicilio domicilio = domicilioService.obtenerDomicilioDeCliente(cliente.getId(), id);

            model.addAttribute("cliente", cliente);
            model.addAttribute("domicilio", domicilio);

            return "cliente/domicilio/form";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cliente/domicilios";
        }
    }

    // GUARDAR (nuevo o editado)
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("domicilio") Domicilio formDomicilio,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null || !(usuario instanceof Cliente cliente)) {
            return "redirect:/usuario/login";
        }

        try {
            domicilioService.guardarDomicilioDeCliente(cliente.getId(), formDomicilio);
            redirectAttributes.addFlashAttribute("exito", "Domicilio guardado correctamente.");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/cliente/domicilios";
    }

    // ELIMINAR
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null || !(usuario instanceof Cliente cliente)) {
            return "redirect:/usuario/login";
        }

        try {
            domicilioService.eliminarDomicilioDeCliente(cliente.getId(), id);
            redirectAttributes.addFlashAttribute("exito", "Domicilio eliminado correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/cliente/domicilios";
    }
}
