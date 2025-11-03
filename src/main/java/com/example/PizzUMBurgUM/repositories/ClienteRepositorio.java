package com.example.PizzUMBurgUM.repositories;

import com.example.PizzUMBurgUM.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, String> {

    List<Cliente> findByNombreContaining(String nombre);

    List<Cliente> findByApellidoContaining(String apellido);

    Cliente findByCedula(Long cedula);

    boolean existsByCedula(Long cedula);

}
