package com.example.PizzUMBurgUM.repositories;

import com.example.PizzUMBurgUM.entities.Domicilio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DomicilioRepository extends JpaRepository<Domicilio, Long> {
    List<Domicilio> findByClienteId(long clienteId);
    void deleteById(long id);
}
