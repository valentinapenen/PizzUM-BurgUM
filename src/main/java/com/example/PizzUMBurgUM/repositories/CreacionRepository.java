package com.example.PizzUMBurgUM.repositories;

import com.example.PizzUMBurgUM.entities.Creacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreacionRepository extends JpaRepository<Creacion, Long> {
    List<Creacion> findByClienteId(long clienteId);
}
