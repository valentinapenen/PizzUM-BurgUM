package com.example.PizzUMBurgUM.repositories;

import com.example.PizzUMBurgUM.entities.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdministradorRepositorio extends JpaRepository<Administrador, String> {

    List<Administrador> findByNombreContaining(String nombre);

    Administrador findByCorreo(String Correo);





}
