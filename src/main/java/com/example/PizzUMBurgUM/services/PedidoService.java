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
        List<Pedido> pedidosActivos = pedidoRepository.findByEstadoNot(EstadoPedido.ENTREGADO);
        return pedidosActivos;
    }

    @Transactional
    public Pedido cambiarEstado(long id, EstadoPedido nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        EstadoPedido actual = pedido.getEstado();

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

    public Pedido obtenerPedido(long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }


}
