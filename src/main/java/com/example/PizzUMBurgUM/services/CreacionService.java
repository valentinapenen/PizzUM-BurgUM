package com.example.PizzUMBurgUM.services;

import com.example.PizzUMBurgUM.entities.*;
import com.example.PizzUMBurgUM.entities.enums.TamanoPizza;
import com.example.PizzUMBurgUM.entities.enums.TipoCreacion;
import com.example.PizzUMBurgUM.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreacionService {

    @Autowired
    private CreacionRepository creacionRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    // Hacer creación
    @Transactional
    public Creacion crearCreacion(long clienteId, TipoCreacion tipo, List<Long> idsProductos, boolean favorito) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        List<Producto> productos = productoRepository.findAllById(idsProductos);
        double total = productos.stream().mapToDouble(Producto::getPrecio).sum();

        Creacion creacion = Creacion.builder()
                .cliente(cliente)
                .tipo(tipo)
                .productos(productos)
                .favorito(favorito)
                .precioTotal(total)
                .build();

        return creacionRepository.save(creacion);
    }

    // Listar todas las creaciones de un cliente
    public List<Creacion> listarPorCliente(Long clienteId) {
        return creacionRepository.findByClienteId(clienteId);
    }

    // Marcar o desmarcar como favorita
    public Creacion marcarFavorito(Long creacionId, boolean favorito) {
        Creacion creacion = creacionRepository.findById(creacionId)
                .orElseThrow(() -> new EntityNotFoundException("Creación no encontrada con ID: " + creacionId));
        creacion.setFavorito(favorito);
        return creacionRepository.save(creacion);
    }

    // Eliminar una creación
    public void eliminarCreacion(Long creacionId) {
        if (!creacionRepository.existsById(creacionId)) {
            throw new EntityNotFoundException("Creación no encontrada con ID: " + creacionId);
        }
        creacionRepository.deleteById(creacionId);
    }
}
