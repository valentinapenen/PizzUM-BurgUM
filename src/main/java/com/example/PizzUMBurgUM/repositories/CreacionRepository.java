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

    // Creaciones visibles en el carrito (flag enCarrito = true)
    List<Creacion> findByClienteIdAndEnCarritoTrue(long clienteId);
}
