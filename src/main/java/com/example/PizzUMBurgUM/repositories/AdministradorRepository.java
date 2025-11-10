package com.example.PizzUMBurgUM.repositories;

import com.example.PizzUMBurgUM.entities.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    List<Administrador> findByNombreContaining(String nombre);

    Administrador findByCedula(String cedula);

    boolean existsByCedula(String cedula);

    Administrador findByCorreo(String correo);
}
