package com.example.PizzUMBurgUM.services;

import com.example.PizzUMBurgUM.entities.Cliente;
import com.example.PizzUMBurgUM.entities.Creacion;
import com.example.PizzUMBurgUM.entities.Domicilio;
import com.example.PizzUMBurgUM.entities.Pedido;
import com.example.PizzUMBurgUM.entities.enums.EstadoPedido;
import com.example.PizzUMBurgUM.entities.enums.MedioDePago;
import com.example.PizzUMBurgUM.repositories.CreacionRepository;
import com.example.PizzUMBurgUM.repositories.PedidoRepository;
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
    private DomicilioService domicilioService;

    public Pedido crearPedido(Cliente cliente, List<Long> idsCreaciones, Domicilio domicilio, MedioDePago medioDePago) {
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setDomicilio(domicilioService.buscarPorId(idDomicilio));
        pedido.setEstado(EstadoPedido.EN_COLA);
        pedido.setFecha(LocalDateTime.now());
        pedido.setMedioDePago(MedioDePago.valueOf(medioDePago.toUpperCase()));

        List<Creacion> creaciones = creacionRepository.findAllById(idsCreaciones);

        double total = 0.0;
        for (Creacion creacion : creaciones) {
            total += creacion.getPrecioTotal();
            creacion.setPedido(pedido);
        }

        pedido.setCreaciones(creaciones);
        pedido.setTotal(total);

        return pedidoRepository.save(pedido);
    }

    public Pedido cambiarEstado(long id, EstadoPedido nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarPorCliente(Cliente cliente) {
        return pedidoRepository.findByClienteId(cliente.getCedula());
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }
}
