package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.entities.Creacion;
import com.example.PizzUMBurgUM.entities.enums.TipoCreacion;
import com.example.PizzUMBurgUM.services.CreacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/creaciones")
@RequiredArgsConstructor
public class CreacionController {

    private final CreacionService creacionService;

    @GetMapping("/nueva")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("tipos", TipoCreacion.values());
        return "crear-creacion"; // formulario Thymeleaf
    }

    @PostMapping
    public String crearCreacion(@RequestParam Long clienteId, @RequestParam TipoCreacion tipo, @RequestParam List<Long> productos, @RequestParam(defaultValue = "false") boolean favorito, Model model) {
        Creacion creacion = creacionService.crearCreacion(clienteId, tipo, productos, favorito);
        model.addAttribute("creacion", creacion);
        return "confirmacion-creacion";
    }
}
