package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.entities.Tarjeta;
//import com.example.PizzUMBurgUM.services.ClienteService;
import com.example.PizzUMBurgUM.services.TarjetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tarjetas")
public class TarjetaController {

    @Autowired
    private TarjetaService tarjetaService;

//    @Autowired
//    private ClienteService clienteService;
//
//    public String crearTarjeta(@RequestParam Long clienteId,
//                               @RequestParam String numero,
//                               @RequestParam String tipoTarjeta,
//                               @RequestParam String fechaVencimiento,
//                               @RequestParam(defaultValue = "false") boolean predeterminada,
//                               Model model) {
//
//        tarjetaService.crearTarjeta(numero, clienteId, tipoTarjeta, fechaVencimiento, predeterminada);
//        return "redirect:/tarjetas/cliente/" + clienteId;
//    }
//
//    @GetMapping("/cliente/{idCliente}")
//    public String listarTarjetas(@PathVariable Long idCliente, Model model) {
//        List<Tarjeta> tarjetas = tarjetaService.listarPorCliente(idCliente);
//        model.addAttribute("tarjetas", tarjetas);
//        model.addAttribute("cliente", clienteService.buscarPorId(idCliente));
//        return "tarjetas/lista"; // → templates/tarjetas/lista.html
//    }

    @GetMapping("/nueva/{idCliente}")
    public String mostrarFormularioNuevaTarjeta(@PathVariable Long idCliente, Model model) {
        model.addAttribute("tarjeta", new Tarjeta());
        model.addAttribute("clienteId", idCliente);
        return "tarjetas/formulario"; // → templates/tarjetas/formulario.html
    }

    @PostMapping("/{idCliente}/predeterminada/{idTarjeta}")
    public String marcarPredeterminada(@PathVariable Long idCliente, @PathVariable Long idTarjeta) {
        tarjetaService.marcarPredeterminada(idCliente, idTarjeta);
        return "redirect:/tarjetas/cliente/" + idCliente;
    }

    @PostMapping("/{idTarjeta}/eliminar")
    public String eliminarTarjeta(@PathVariable Long idTarjeta, @RequestParam Long clienteId) {
        tarjetaService.eliminarTarjeta(idTarjeta);
        return "redirect:/tarjetas/cliente/" + clienteId;
    }
}
