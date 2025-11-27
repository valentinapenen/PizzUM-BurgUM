package com.example.PizzUMBurgUM.repositories;

import com.example.PizzUMBurgUM.entities.Pedido;
import com.example.PizzUMBurgUM.entities.enums.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteId(long clienteId);
    List<Pedido> findByFechaBetween(LocalDateTime desde, LocalDateTime hasta);
    List<Pedido> findByFechaBetweenAndEstado(LocalDateTime desde, LocalDateTime hasta, EstadoPedido estado);
    List<Pedido> findByEstadoNot(EstadoPedido estado);
    List<Pedido> findByEstadoNotIn(List<EstadoPedido> estados);


    //hay un valor T (por ejemplo un Pedido)
    //o no hay nada (equivalente a null)
    Optional<Pedido> findByClienteIdAndEstado(Long clienteId, EstadoPedido estado);

    Optional<Pedido> findFirstByClienteIdAndEstadoInOrderByFechaDesc(Long clienteId, java.util.List<EstadoPedido> estados);
}
