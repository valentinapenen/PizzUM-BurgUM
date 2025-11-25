package com.example.PizzUMBurgUM.services;

import com.example.PizzUMBurgUM.entities.PrecioTamanoPizza;
import com.example.PizzUMBurgUM.entities.enums.TamanoPizza;
import com.example.PizzUMBurgUM.repositories.PrecioTamanoPizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrecioTamanoPizzaService {

    @Autowired
    private PrecioTamanoPizzaRepository repo;

    public double obtenerPrecioPorTamano(TamanoPizza tamano) {
        return repo.findByTamano(tamano)
                .map(PrecioTamanoPizza::getPrecioBase)
                .orElseGet(() -> {
                    // Si no existe en DB, crear registro usando el valor por defecto del enum
                    PrecioTamanoPizza nuevo = PrecioTamanoPizza.builder()
                            .tamano(tamano)
                            .precioBase(tamano.getPrecioBase())
                            .build();
                    repo.save(nuevo);
                    return nuevo.getPrecioBase();
                });
    }

    public List<PrecioTamanoPizza> listarTodos() {
        // Asegurarnos de que existan los 3 registros
        List<PrecioTamanoPizza> existentes = new ArrayList<>(repo.findAll());
        for (TamanoPizza t : TamanoPizza.values()) {
            if (existentes.stream().noneMatch(p -> p.getTamano() == t)) {
                existentes.add(repo.save(PrecioTamanoPizza.builder()
                        .tamano(t)
                        .precioBase(t.getPrecioBase())
                        .build()));
            }
        }
        return existentes;
    }

    public void actualizarPrecio(TamanoPizza tamano, double nuevoPrecio) {
        if (nuevoPrecio <= 0) throw new IllegalArgumentException("El precio debe ser mayor a 0");
        PrecioTamanoPizza entidad = repo.findByTamano(tamano)
                .orElseGet(() -> PrecioTamanoPizza.builder()
                        .tamano(tamano)
                        .precioBase(tamano.getPrecioBase())
                        .build());
        entidad.setPrecioBase(nuevoPrecio);
        repo.save(entidad);
    }
}
