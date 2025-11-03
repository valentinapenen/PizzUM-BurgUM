package com.example.PizzUMBurgUM.repositories;

import com.example.PizzUMBurgUM.entities.Tarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarjetaRepository extends JpaRepository<Tarjeta, Long> {
    List<Tarjeta> findByClienteId(long clienteId);
    void deleteById(long id);
}
