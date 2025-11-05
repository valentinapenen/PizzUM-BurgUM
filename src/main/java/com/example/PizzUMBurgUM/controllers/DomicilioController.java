package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.controllers.DTOS.DomicilioRequest;
import com.example.PizzUMBurgUM.entities.Cliente;
import com.example.PizzUMBurgUM.entities.Domicilio;
import com.example.PizzUMBurgUM.services.ClienteServicio;
import com.example.PizzUMBurgUM.services.DomicilioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Controller

public class DomicilioController {
    @Autowired
    private DomicilioService domicilioService;

    @Autowired
    private ClienteServicio clienteService;


    @GetMapping("/nuevo/{idCliente}")
    public String mostrarFormularioNuevo(@PathVariable Long idCliente, Model model) {
        model.addAttribute("domicilio", new Domicilio());
        model.addAttribute("clienteId", idCliente);
        return "domicilios/formulario"; // → templates/domicilios/formulario.html
    }

    @PostMapping("/crear")
    public String crearDomicilio(@RequestParam String clienteId,
                                 @RequestParam String calle,
                                 @RequestParam String numero,
                                 @RequestParam String ciudad,
                                 @RequestParam String departamento,
                                 @RequestParam(required = false) String apartamento,
                                 @RequestParam(defaultValue = "false") boolean predeterminado) {

        domicilioService.crearDomicilio(clienteId, calle, numero, ciudad, departamento, apartamento, predeterminado);
        return "redirect:/domicilios/cliente/" + clienteId;
    }

    @PostMapping("/{idDomicilio}/eliminar")
    public String eliminarDomicilio(@PathVariable Long idDomicilio, @RequestParam Long clienteId) {
        domicilioService.eliminarDomicilio(idDomicilio);
        return "redirect:/domicilios/cliente/" + clienteId;
    }

    @PostMapping("/{idCliente}/predeterminado/{idDomicilio}")
    public String marcarPredeterminado(@PathVariable String idCliente, @PathVariable Long idDomicilio) {
        domicilioService.marcarPredeterminado(idCliente, idDomicilio);
        return "redirect:/domicilios/cliente/" + idCliente;
    }

    @GetMapping("/cliente/{idCliente}")
    public String listarPorCliente(@PathVariable String idCliente, Model model) {
        Cliente cliente = clienteService.buscarPorId(idCliente);
        List<Domicilio> domicilios = domicilioService.listarPorCliente(cliente);

        model.addAttribute("cliente", cliente);
        model.addAttribute("domicilios", domicilios);
        return "domicilios/lista"; // → templates/domicilios/lista.html
    }
}
