package com.example.PizzUMBurgUM.repositories;

import com.example.PizzUMBurgUM.entities.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdministradorRepositorio extends JpaRepository<Administrador, String> {

    List<Administrador> findByNombreContaining(String nombre);

    Administrador findByCedula(Long cedula);

    boolean existsByCedula(Long cedula);


}
