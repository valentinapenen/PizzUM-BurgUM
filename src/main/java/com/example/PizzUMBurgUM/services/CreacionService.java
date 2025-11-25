package com.example.PizzUMBurgUM.services;

import com.example.PizzUMBurgUM.entities.*;
import com.example.PizzUMBurgUM.entities.enums.EstadoPedido;
import com.example.PizzUMBurgUM.entities.enums.MedioDePago;
import com.example.PizzUMBurgUM.entities.enums.TamanoPizza;
import com.example.PizzUMBurgUM.entities.enums.TipoCreacion;
import com.example.PizzUMBurgUM.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CreacionService {

    @Autowired
    private CreacionRepository creacionRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private DomicilioRepository domicilioRepository;
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private TarjetaRepository tarjetaRepository;


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


    public List<Creacion> listarFavoritosPorCliente(Long clienteId) {
        return creacionRepository.findByClienteIdAndFavoritoTrue(clienteId);
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


    @Transactional
    public Creacion crearPizzaCompleta(Long clienteId,
                                       Long masaId,
                                       Long salsaId,
                                       Long quesoId,
                                       java.util.List<Long> toppingIds,
                                       java.util.List<Long> bebidaIds,
                                       java.util.List<Long> papasIds,
                                       boolean favorito) {

        java.util.List<Long> productosIds = new java.util.ArrayList<>();
        productosIds.add(masaId);
        productosIds.add(salsaId);
        productosIds.add(quesoId);

        if (toppingIds != null) {
            productosIds.addAll(toppingIds);
        }
        if (bebidaIds != null) {
            productosIds.addAll(bebidaIds);
        }
        if (papasIds != null) {
            productosIds.addAll(papasIds);
        }

        // Reutilizamos la lógica genérica
        return crearCreacion(clienteId, TipoCreacion.PIZZA, productosIds, favorito);
    }

    @Transactional
    public Creacion crearHamburguesaCompleta(Long clienteId,
                                             Long panId,
                                             Long carneId,
                                             Long quesoId,
                                             java.util.List<Long> aderezoIds,
                                             java.util.List<Long> toppingIds,
                                             java.util.List<Long> bebidaIds,
                                             java.util.List<Long> papasIds,
                                             boolean favorito) {

        java.util.List<Long> productosIds = new java.util.ArrayList<>();
        productosIds.add(panId);
        productosIds.add(carneId);
        productosIds.add(quesoId);

        if (aderezoIds != null) {
            productosIds.addAll(aderezoIds);
        }
        if (toppingIds != null) {
            productosIds.addAll(toppingIds);
        }
        if (bebidaIds != null) {
            productosIds.addAll(bebidaIds);
        }
        if (papasIds != null) {
            productosIds.addAll(papasIds);
        }

        // reaprovechamos la lógica genérica
        return crearCreacion(clienteId, TipoCreacion.HAMBURGUESA, productosIds, favorito);
    }

    @Transactional
    public void vaciarCarritoCliente(Long clienteId) {
        List<Creacion> creaciones = creacionRepository.findByClienteId(clienteId);
        creacionRepository.deleteAll(creaciones);
    }


    @Transactional
    public Pedido crearPedidoDesdeCarrito(Cliente cliente,
                                          Long domicilioId,
                                          Long tarjetaId) {

        // 1) Ver creaciones del cliente (carrito)
        List<Creacion> creaciones = creacionRepository.findByClienteId(cliente.getId());
        if (creaciones.isEmpty()) {
            throw new IllegalStateException("El carrito está vacío");
        }

        // 2) Buscar domicilio
        Domicilio domicilio = domicilioRepository.findById(domicilioId)
                .orElseThrow(() -> new RuntimeException("Domicilio no encontrado"));

        // 3) Buscar tarjeta
        Tarjeta tarjeta = tarjetaRepository.findById(tarjetaId)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));

        // 4) Calcular total
        double total = creaciones.stream()
                .mapToDouble(Creacion::getPrecioTotal)
                .sum();

        // 5) Crear pedido
        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .creaciones(creaciones)
                .domicilio(domicilio)
                .medioDePago(MedioDePago.TARJETA)
                .estado(EstadoPedido.EN_COLA)
                .fecha(LocalDateTime.now())
                .total(total)
                .build();

        // 6) Guardar pedido
        Pedido guardado = pedidoRepository.save(pedido);

        // 7) Vaciar carrito del cliente
        creacionRepository.deleteAll(creaciones);

        return guardado;
    }








}
