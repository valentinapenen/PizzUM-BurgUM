package com.example.PizzUMBurgUM.services;

import com.example.PizzUMBurgUM.entities.Producto;
import com.example.PizzUMBurgUM.entities.enums.CategoriaProducto;
import com.example.PizzUMBurgUM.entities.enums.TipoProducto;
import com.example.PizzUMBurgUM.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Producto buscarPorId(long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    public Producto crear(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El producto debe tener nombre");
        }

        if (producto.getPrecio() <= 0) {
            throw new IllegalArgumentException("El producto debe tener un precio válido");
        }

        // disponible = true por defecto
        producto.setDisponible(true);
        return productoRepository.save(producto);
    }

    public Producto actualizarPrecio(long id, double nuevoPrecio) {
        Producto producto = buscarPorId(id);

        if (nuevoPrecio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }

        producto.setPrecio(nuevoPrecio);
        return productoRepository.save(producto);
    }

    public void desactivar(long id) {
        Producto producto = buscarPorId(id);
        producto.setDisponible(false);
        productoRepository.save(producto);
    }

    public void activar(long id) {
        Producto producto = buscarPorId(id);
        producto.setDisponible(true);
        productoRepository.save(producto);
    }

    public List<Producto> listarPorTipoYCategoria(TipoProducto tipo, CategoriaProducto categoria) {
        return productoRepository.findByTipoAndCategoriaInAndDisponibleTrue(
                tipo,
                List.of(categoria, CategoriaProducto.AMBOS)
        );
    }

}
