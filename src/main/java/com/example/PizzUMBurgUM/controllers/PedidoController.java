package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.entities.Pedido;
import com.example.PizzUMBurgUM.entities.enums.EstadoPedido;
import com.example.PizzUMBurgUM.entities.enums.MedioDePago;
import com.example.PizzUMBurgUM.services.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // Listar todos los pedidos
    @GetMapping
    public String listarPedidos(Model model) {
        model.addAttribute("pedidos", pedidoService.listarActivos());
        return "pedido/lista";
    }

    @PostMapping
    public String crearPedido(@RequestParam Long clienteId, @RequestParam List<Long> creaciones, @RequestParam Long domicilioId, @RequestParam MedioDePago medioPago, Model model) {
        Pedido pedido = pedidoService.crearPedido(clienteId, creaciones, domicilioId, medioPago);
        model.addAttribute("pedido", pedido);

        return "pedido/form";
    }

    @PutMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Long id, @RequestParam EstadoPedido estado, Model model) {
        Pedido pedido = pedidoService.cambiarEstado(id, estado);
        model.addAttribute("pedido", pedido);

        return "pedido/form";
    }

}
