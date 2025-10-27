package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.entities.Topping;
import com.example.PizzUMBurgUM.entities.enums.CategoriaTopping;
import com.example.PizzUMBurgUM.entities.enums.TipoTopping;
import com.example.PizzUMBurgUM.services.ToppingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ToppingController {

    @Autowired
    private ToppingService toppingService;

    @GetMapping
    public ResponseEntity<List<Topping>> listarToppingsPorCategoriaYTipo(@PathVariable CategoriaTopping categoriaTopping, @PathVariable TipoTopping tipo) {
        return ResponseEntity.ok(toppingService.listarToppingsPorCategoriaYTipo(categoriaTopping, tipo));
    }
}
