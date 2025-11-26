package com.example.PizzUMBurgUM.repositories;

import com.example.PizzUMBurgUM.entities.PrecioTamanoPizza;
import com.example.PizzUMBurgUM.entities.enums.TamanoPizza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrecioTamanoPizzaRepository extends JpaRepository<PrecioTamanoPizza, Long> {
    Optional<PrecioTamanoPizza> findByTamano(TamanoPizza tamano);
}
