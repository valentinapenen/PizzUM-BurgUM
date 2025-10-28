package com.example.PizzUMBurgUM.services;

import com.example.PizzUMBurgUM.entities.Topping;
import com.example.PizzUMBurgUM.entities.enums.CategoriaTopping;
import com.example.PizzUMBurgUM.entities.enums.TipoTopping;
import com.example.PizzUMBurgUM.repositories.ToppingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class ToppingService {
    @Autowired
    public ToppingRepository toppingRepository;

    public List<Topping> listarToppingsPorCategoriaYTipo(@PathVariable CategoriaTopping categoria, @PathVariable TipoTopping tipo) {
        return toppingRepository.findByCategoriaAndTipo(categoria, tipo);
    }
}
