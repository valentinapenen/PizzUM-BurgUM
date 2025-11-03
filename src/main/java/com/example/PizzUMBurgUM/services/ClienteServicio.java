package com.example.PizzUMBurgUM.services;


import com.example.PizzUMBurgUM.repositories.ClienteRepositorio;
import com.example.PizzUMBurgUM.repositories.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private ClienteRepositorio clienteRepositorio;



}
