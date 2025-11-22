package com.example.PizzUMBurgUM.repositories;

import com.example.PizzUMBurgUM.entities.Tarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarjetaRepository extends JpaRepository<Tarjeta, Long> {

    // Buscar todas las tarjetas de un cliente
    List<Tarjeta> findByClienteId(long clienteId);

    // Buscar una tarjeta por su número
    Tarjeta findByNumero(String numero);

    void deleteById(long id);
}

