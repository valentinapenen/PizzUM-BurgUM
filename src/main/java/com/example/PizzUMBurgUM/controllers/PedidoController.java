package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.controllers.DTOS.PedidoRequest;
import com.example.PizzUMBurgUM.entities.Cliente;
import com.example.PizzUMBurgUM.entities.Pedido;
import com.example.PizzUMBurgUM.entities.enums.EstadoPedido;
import com.example.PizzUMBurgUM.services.ClienteService;
import com.example.PizzUMBurgUM.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

//    @Autowired
//    private ClienteService clienteService;

//    @GetMapping("/cliente/{idCliente}")
//    public String listarPorCliente(@PathVariable Long idCliente, Model model) {
//        Cliente cliente = clienteService.buscarPorId(idCliente);
//        List<Pedido> pedidos = pedidoService.listarPorCliente(cliente);
//
//        model.addAttribute("cliente", cliente);
//        model.addAttribute("pedidos", pedidos);
//        return "pedidos/lista"; // → templates/pedidos/lista.html
//    }

//    @GetMapping("/nuevo/{idCliente}")
//    public String mostrarFormularioPedido(@PathVariable Long idCliente, Model model) {
//        Cliente cliente = clienteService.buscarPorId(idCliente);
//        model.addAttribute("cliente", cliente);
//        model.addAttribute("pedido", new Pedido());
//        return "pedidos/formulario"; // → templates/pedidos/formulario.html
//    }

//    @PostMapping("/crear")
//    public String crearPedido(@RequestParam Long idCliente,
//                              @RequestParam Long idDomicilio,
//                              @RequestParam String medioDePago,
//                              Model model) {
//
//        Cliente cliente = clienteService.buscarPorId(idCliente);
//        pedidoService.crearPedidoDesdeFormulario(cliente, idDomicilio, medioDePago);
//
//        return "redirect:/pedidos/cliente/" + idCliente;
//    }

    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Long id,
                                @RequestParam EstadoPedido nuevoEstado,
                                Model model) {
        pedidoService.cambiarEstado(id, nuevoEstado);
        return "redirect:/admin/pedidos";
    }

    @GetMapping("/admin")
    public String listarTodos(Model model) {
        List<Pedido> pedidos = pedidoService.listarTodos();
        model.addAttribute("pedidos", pedidos);
        return "pedidos/admin-lista"; // → templates/pedidos/admin-lista.html
    }
}
