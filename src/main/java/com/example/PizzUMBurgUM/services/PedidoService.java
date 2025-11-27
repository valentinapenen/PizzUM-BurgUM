package com.example.PizzUMBurgUM.services;

import com.example.PizzUMBurgUM.entities.Cliente;
import com.example.PizzUMBurgUM.entities.Creacion;
import com.example.PizzUMBurgUM.entities.Domicilio;
import com.example.PizzUMBurgUM.entities.Pedido;
import com.example.PizzUMBurgUM.entities.enums.EstadoPedido;
import com.example.PizzUMBurgUM.entities.enums.MedioDePago;
import com.example.PizzUMBurgUM.repositories.ClienteRepository;
import com.example.PizzUMBurgUM.repositories.CreacionRepository;
import com.example.PizzUMBurgUM.repositories.DomicilioRepository;
import com.example.PizzUMBurgUM.repositories.PedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private CreacionRepository creacionRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private DomicilioRepository domicilioRepository;



    @Transactional
    public Pedido crearPedido(long clienteId, List<Long> idsCreaciones, long domicilioId, MedioDePago medioPago) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Domicilio domicilio = domicilioRepository.findById(domicilioId)
                .orElseThrow(() -> new RuntimeException("Domicilio no encontrado"));

        List<Creacion> creaciones = creacionRepository.findAllById(idsCreaciones);

        double total = creaciones.stream().mapToDouble(Creacion::getPrecioTotal).sum();

        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .creaciones(creaciones)
                .domicilio(domicilio)
                .medioDePago(medioPago)
                .estado(EstadoPedido.EN_PREPARACION)
                .fecha(LocalDateTime.now())
                .total(total)
                .build();

        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarActivos() {
        // Excluir pedidos ENTREGADO y CANCELADO del listado de administración
        List<Pedido> pedidosActivos = pedidoRepository.findByEstadoNotIn(
                java.util.List.of(EstadoPedido.ENTREGADO, EstadoPedido.CANCELADO)
        );
        return pedidosActivos;
    }

    @Transactional
    public Pedido cambiarEstado(long id, EstadoPedido nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        EstadoPedido actual = pedido.getEstado();

        // Si el pedido fue cancelado por el cliente, no se permite modificar su estado
        if (actual == EstadoPedido.CANCELADO) {
            throw new IllegalStateException("No se puede modificar un pedido cancelado.");
        }

        if (actual == EstadoPedido.EN_COLA && nuevoEstado == EstadoPedido.CANCELADO) {
            pedido.setEstado(nuevoEstado);
            return pedidoRepository.save(pedido);
        }
        if (nuevoEstado.ordinal() < actual.ordinal()) {
            throw new IllegalArgumentException("No se puede retroceder el estado del pedido.");
        }

        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarPorCliente(long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    public List<Pedido> listarPedidosPorFecha(LocalDateTime desde, LocalDateTime hasta) {
        return pedidoRepository.findByFechaBetween(desde, hasta);
    }

    public List<Pedido> listarPedidosPorFechaYEstado(LocalDateTime desde, LocalDateTime hasta, EstadoPedido estado) {
        if (estado == null) {
            return listarPedidosPorFecha(desde, hasta);
        }
        return pedidoRepository.findByFechaBetweenAndEstado(desde, hasta, estado);
    }

    public Pedido obtenerPedido(long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }


    public java.util.Optional<Pedido> buscarPedidoEnCurso(Long clienteId) {
        java.util.List<EstadoPedido> estadosEnCurso = java.util.List.of(
                EstadoPedido.EN_COLA,
                EstadoPedido.EN_PREPARACION,
                EstadoPedido.EN_CAMINO
        );

        return pedidoRepository.findFirstByClienteIdAndEstadoInOrderByFechaDesc(
                clienteId,
                estadosEnCurso
        );
    }


    public boolean clienteTienePedidoEnCurso(Long clienteId) {
        return buscarPedidoEnCurso(clienteId).isPresent();
    }

    @Transactional
    public Pedido cancelarPedido(Long clienteId, Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (!Objects.equals(pedido.getCliente().getId(), clienteId)) {
            throw new IllegalArgumentException("El pedido no pertenece al cliente");
        }

        // Sólo permitir cancelar si aún no fue entregado ni va en camino avanzado
        if (pedido.getEstado() != EstadoPedido.EN_COLA) {
            throw new IllegalStateException("El pedido no se puede cancelar en su estado actual");
        }

        // Eliminar las creaciones no favoritas asociadas a este pedido
        List<Creacion> creaciones = pedido.getCreaciones();

        // Primero procesamos favoritas (no se eliminan) y vamos recolectando las no favoritas
        java.util.List<Creacion> aEliminar = new java.util.ArrayList<>();
        for (Creacion c : creaciones) {
            if (c.isFavorito()) {
                // Si era favorita, asegurar que no quede en carrito
                c.setEnCarrito(false);
                creacionRepository.save(c);
            } else {
                aEliminar.add(c);
            }
        }

        // Desvincular las no favoritas del pedido para evitar problemas con la join table
        if (!aEliminar.isEmpty()) {
            creaciones.removeAll(aEliminar);
            pedido.setCreaciones(creaciones);
            pedidoRepository.save(pedido);
            // Ahora sí, eliminarlas físicamente
            creacionRepository.deleteAll(aEliminar);
        }

        pedido.setEstado(EstadoPedido.CANCELADO);
        return pedidoRepository.save(pedido);
    }



}
