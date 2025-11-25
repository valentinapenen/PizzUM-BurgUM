package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.entities.Pedido;
import com.example.PizzUMBurgUM.entities.enums.EstadoPedido;
import com.example.PizzUMBurgUM.entities.enums.MedioDePago;
import com.example.PizzUMBurgUM.services.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/pedidos")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // Listar todos los pedidos
    @GetMapping
    public String listarPedidos(Model model) {
        model.addAttribute("pedidos", pedidoService.listarActivos());
        return "administrador/pedido/lista";
    }

    @PostMapping
    public String crearPedido(@RequestParam Long clienteId, @RequestParam List<Long> creaciones, @RequestParam Long domicilioId, @RequestParam MedioDePago medioPago, Model model) {
        Pedido pedido = pedidoService.crearPedido(clienteId, creaciones, domicilioId, medioPago);
        model.addAttribute("pedido", pedido);

        return "administrador/pedido/form";
    }

    @PutMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Long id, @RequestParam EstadoPedido estado, Model model) {
        Pedido pedido = pedidoService.cambiarEstado(id, estado);
        model.addAttribute("pedido", pedido);

        return "administrador/pedido/lista";
    }

    // Soporte para formularios HTML (POST) desde la vista de administración
    @PostMapping("/{id}/estado")
    public String cambiarEstadoPost(@PathVariable Long id,
                                    @RequestParam("estado") EstadoPedido estado,
                                    RedirectAttributes redirectAttributes) {
        try {
            pedidoService.cambiarEstado(id, estado);
            redirectAttributes.addFlashAttribute("exito", "Estado actualizado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/pedidos";
    }

}
