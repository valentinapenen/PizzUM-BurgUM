package com.example.PizzUMBurgUM.services;


import com.example.PizzUMBurgUM.controllers.DTOS.RegistroClienteRequest;
import com.example.PizzUMBurgUM.entities.Cliente;
import com.example.PizzUMBurgUM.entities.Domicilio;
import com.example.PizzUMBurgUM.entities.Tarjeta;
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
    @Autowired
    private UsuarioServicio usuarioServicio;


    public Cliente regisatrarCliente(RegistroClienteRequest registroClienteRequest){

        Cliente cliente = new Cliente();
        cliente.setNombre(registroClienteRequest.getNombre());
        cliente.setApellido(registroClienteRequest.getApellido());
        cliente.setCedula(registroClienteRequest.getCedula());
        cliente.setFechaNacimiento(registroClienteRequest.getFechaNacimiento());
        cliente.setCorreo(registroClienteRequest.getCorreo());
        cliente.setTelefono(registroClienteRequest.getTelefono());
        cliente.setContrasena(registroClienteRequest.getContrasena());

        Domicilio domicilio = new Domicilio();
        domicilio.setCalle(registroClienteRequest.getDomicilio().getCalle());
        domicilio.setNumero(registroClienteRequest.getDomicilio().getNumero());
        domicilio.setDepartamento(registroClienteRequest.getDomicilio().getDepartamento());
        domicilio.setCiudad(registroClienteRequest.getDomicilio().getCiudad());
        domicilio.setApartamento(registroClienteRequest.getDomicilio().getApartamento());
        domicilio.setPredeterminado(true);
        domicilio.setCliente(cliente);
        cliente.getDomicilios().add(domicilio);

        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setNumeroEnmascarado(registroClienteRequest.getTarjeta().getNumero());
        tarjeta.setNombreTitular(registroClienteRequest.getTarjeta().getNombreTitular());
        tarjeta.setCliente(cliente);
        tarjeta.setTipoTarjeta(registroClienteRequest.getTarjeta().getTipoTarjeta());
        tarjeta.setFecha_vencimiento(registroClienteRequest.getTarjeta().getFechaVencimiento());
        tarjeta.setPredeterminada(true);
        cliente.getTarjetas().add(tarjeta);

        return usuarioServicio.registrarCliente(cliente);
        
    }

}
