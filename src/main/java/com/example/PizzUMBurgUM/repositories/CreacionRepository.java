package com.example.PizzUMBurgUM.repositories;

import com.example.PizzUMBurgUM.entities.Creacion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreacionRepository extends JpaRepository<Creacion, Long> {
    List<Creacion> findByClienteId(long clienteId);

    List<Creacion> findByClienteIdAndFavoritoTrue(long clienteId);

    /**
     * Creaciones que pertenecen al cliente y que NO están asociadas a ningún Pedido
     * (es decir, siguen en el carrito).
     */
    @Query("select c from Creacion c where c.cliente.id = :clienteId and c.id not in (select cr.id from Pedido p join p.creaciones cr)")
    List<Creacion> findCarritoByClienteId(@Param("clienteId") long clienteId);
}
