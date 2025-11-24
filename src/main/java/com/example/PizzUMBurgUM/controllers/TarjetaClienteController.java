package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.entities.Cliente;
import com.example.PizzUMBurgUM.entities.Tarjeta;
import com.example.PizzUMBurgUM.entities.enums.TipoTarjeta;
import com.example.PizzUMBurgUM.services.TarjetaService;
import com.example.PizzUMBurgUM.services.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cliente/tarjetas")
public class TarjetaClienteController {

    @Autowired
    private TarjetaService tarjetaService;

    @Autowired
    private ClienteService clienteService;

    //lista de tarjetas
    @GetMapping
    public String listarTarjetas(HttpSession session, Model model) {

        Cliente cliente = (Cliente) session.getAttribute("usuarioLogueado");
        if (cliente == null) return "redirect:/usuario/login";

        model.addAttribute("cliente", cliente);
        model.addAttribute("tarjetas",
                tarjetaService.listarTarjetasPorCliente(cliente.getId()));

        return "tarjetas/lista";
    }

    // para nuev atarjeta
    @GetMapping("/nueva")
    public String nuevaTarjeta(HttpSession session, Model model) {

        Cliente cliente = (Cliente) session.getAttribute("usuarioLogueado");
        if (cliente == null) return "redirect:/usuario/login";

        model.addAttribute("tarjeta", new Tarjeta());
        model.addAttribute("tipos", TipoTarjeta.values());
        return "tarjetas/form";
    }

    //para editar tarjeta
    @GetMapping("/{id}/editar")
    public String editarTarjeta(@PathVariable long id,
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        Cliente cliente = (Cliente) session.getAttribute("usuarioLogueado");
        if (cliente == null) return "redirect:/usuario/login";

        Tarjeta tarjeta = tarjetaService.buscarPorIdDeCliente(cliente.getId(), id);
        if (tarjeta == null) {
            redirectAttributes.addFlashAttribute("error", "La tarjeta no pertenece al cliente.");
            return "redirect:/cliente/tarjetas";
        }

        model.addAttribute("tarjeta", tarjeta);
        model.addAttribute("tipos", TipoTarjeta.values());
        return "tarjetas/form";
    }

    //guardar crear o editar tarjeta
    @PostMapping("/guardar")
    public String guardar(
            @ModelAttribute("tarjeta") Tarjeta datos,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Cliente cliente = (Cliente) session.getAttribute("usuarioLogueado");
        if (cliente == null) return "redirect:/usuario/login";

        try {
            tarjetaService.guardarTarjetaDeCliente(cliente.getId(), datos);
            redirectAttributes.addFlashAttribute("exito", "Tarjeta guardada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/cliente/tarjetas";
    }

    //eliminar tajeta
    @PostMapping("/{id}/eliminar")
    public String eliminar(
            @PathVariable long id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Cliente cliente = (Cliente) session.getAttribute("usuarioLogueado");
        if (cliente == null) return "redirect:/usuario/login";

        try {
            tarjetaService.eliminarTarjetaDeCliente(cliente.getId(), id);
            redirectAttributes.addFlashAttribute("exito", "Tarjeta eliminada.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/cliente/tarjetas";
    }

    //marcar predeterminada
    @PostMapping("/{id}/predeterminada")
    public String marcarPredeterminada(
            @PathVariable long id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Cliente cliente = (Cliente) session.getAttribute("usuarioLogueado");
        if (cliente == null) return "redirect:/usuario/login";

        try {
            tarjetaService.marcarPredeterminada(cliente.getId(), id);
            redirectAttributes.addFlashAttribute("exito", "Tarjeta marcada como predeterminada.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/cliente/tarjetas";
    }
}

