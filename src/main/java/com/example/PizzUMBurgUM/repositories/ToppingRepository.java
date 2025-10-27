package com.example.PizzUMBurgUM.repositories;

import com.example.PizzUMBurgUM.entities.Topping;
import com.example.PizzUMBurgUM.entities.enums.CategoriaTopping;
import com.example.PizzUMBurgUM.entities.enums.TipoTopping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToppingRepository extends JpaRepository<Topping, Long> {
    List<Topping> findByCategoria(CategoriaTopping categoria);
    List<Topping> findByCategoriaAndTipo(CategoriaTopping categoria, TipoTopping tipo);
}
