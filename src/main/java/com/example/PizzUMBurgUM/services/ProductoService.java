package com.example.PizzUMBurgUM.services;

import com.example.PizzUMBurgUM.entities.Producto;
import com.example.PizzUMBurgUM.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarActivos() {
        return productoRepository.findByActivoTrue();
    }

    public Producto crear(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El producto debe tener nombre");
        }
        if (producto.getPrecioBase() <= 0) {
            throw new IllegalArgumentException("El producto debe tener un precio válido");
        }
        return productoRepository.save(producto);
    }

    public Producto actualizarPrecio(long id, double nuevoPrecio) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El producto no existe"));
        producto.setPrecioBase(nuevoPrecio);
        return productoRepository.save(producto);
    }

    public void desactivar(long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El producto no existe"));
        producto.setDisponible(false);
        productoRepository.save(producto);
    }

    public void activar(long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El producto no existe"));
        producto.setDisponible(true);
        productoRepository.save(producto);
    }
}
