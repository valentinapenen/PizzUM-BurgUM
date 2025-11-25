package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.entities.PrecioTamanoPizza;
import com.example.PizzUMBurgUM.entities.enums.TamanoPizza;
import com.example.PizzUMBurgUM.services.PrecioTamanoPizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/pizzas/precios")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPrecioPizzaController {

    @Autowired
    private PrecioTamanoPizzaService precioService;

    @GetMapping
    public String verPrecios(Model model) {
        List<PrecioTamanoPizza> precios = precioService.listarTodos();
        model.addAttribute("precios", precios);
        return "administrador/producto/precios-pizza";
    }

    @PostMapping
    public String actualizarPrecios(
            @RequestParam("precioChica") double precioChica,
            @RequestParam("precioMediana") double precioMediana,
            @RequestParam("precioGrande") double precioGrande
    ) {
        precioService.actualizarPrecio(TamanoPizza.CHICA, precioChica);
        precioService.actualizarPrecio(TamanoPizza.MEDIANA, precioMediana);
        precioService.actualizarPrecio(TamanoPizza.GRANDE, precioGrande);
        return "redirect:/admin/pizzas/precios";
    }
}
