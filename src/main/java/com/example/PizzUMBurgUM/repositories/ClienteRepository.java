package com.example.PizzUMBurgUM.repositories;

import com.example.PizzUMBurgUM.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByNombreContaining(String nombre);

    List<Cliente> findByApellidoContaining(String apellido);

    boolean existsByCedula(String cedula);

    Cliente findByCorreo(String correo);


}
