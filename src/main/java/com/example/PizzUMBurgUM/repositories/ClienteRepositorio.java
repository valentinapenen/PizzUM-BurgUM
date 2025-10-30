package com.example.PizzUMBurgUM.repositories;

import com.example.PizzUMBurgUM.entities.Cliente;

import java.util.List;

public interface ClienteRepositorio {

    List<Cliente> findByNombreContaining(String nombre);

    List<Cliente> findByApellidoContaining(String apellido);

    Cliente findByCorreo(String Correo);

    Cliente findByTelefono(String Telefono);


}
