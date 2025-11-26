package com.example.PizzUMBurgUM.services;

import com.example.PizzUMBurgUM.entities.Administrador;
import com.example.PizzUMBurgUM.entities.Cliente;
import com.example.PizzUMBurgUM.entities.Usuario;
import com.example.PizzUMBurgUM.repositories.AdministradorRepository;
import com.example.PizzUMBurgUM.repositories.ClienteRepository;
import com.example.PizzUMBurgUM.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    @Autowired
    private AdministradorRepository administradorRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;


    public Usuario login(String correo, String password){
        Administrador admin = administradorRepository.findByCorreo(correo); //Saque el orElse(null) porque si no lo encuentra ya retorna null
        if(admin != null){
            if(admin.getContrasena() != null && admin.getContrasena().equals(password)){
                return admin;
            }
            else throw new IllegalArgumentException("Contraseña incorrecta.");
        }

        Cliente cliente = clienteRepository.findByCorreo(correo);  //Saque el orElse(null) porque si no lo encuentra ya retorna null
        if (cliente != null){
            if  (cliente.getContrasena() != null && cliente.getContrasena().equals(password)){
                return cliente;
            }
            else throw new IllegalArgumentException("Contraseña incorrecta.");
        }

        throw new  IllegalArgumentException("No existe un usuario con este correo.");
    }

    public boolean correoEnUso(String correo){
        if (usuarioRepository.existsByCorreo(correo)){
            return true;
        }
        else return false;
    }



    public long contarAdministradores() {
        return administradorRepository.count();
    }

    public long contarClientes() {
        return clienteRepository.count();
    }

    public long contarUsuarios() {
        return administradorRepository.count() + clienteRepository.count();
    }




}
