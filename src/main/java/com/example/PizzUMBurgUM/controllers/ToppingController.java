package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.entities.Topping;
import com.example.PizzUMBurgUM.entities.enums.CategoriaTopping;
import com.example.PizzUMBurgUM.entities.enums.TipoTopping;
import com.example.PizzUMBurgUM.services.ToppingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/toppings")
public class ToppingController {

    @Autowired
    private ToppingService toppingService;

    @GetMapping("/{categoria}/{tipo}")
    public String listarToppingsPorCategoriaYTipo(
            @PathVariable("categoria") CategoriaTopping categoriaTopping,
            @PathVariable("tipo") TipoTopping tipo,
            Model model) {

        List<Topping> toppings = toppingService.listarToppingsPorCategoriaYTipo(categoriaTopping, tipo);
        model.addAttribute("toppings", toppings);
        model.addAttribute("categoria", categoriaTopping);
        model.addAttribute("tipo", tipo);
        return "toppings/lista"; // → templates/toppings/lista.html
    }

    @GetMapping
    public String listarTodos(Model model) {
        List<Topping> toppings = toppingService.listarTodos();
        model.addAttribute("toppings", toppings);
        return "toppings/lista";
    }
}
