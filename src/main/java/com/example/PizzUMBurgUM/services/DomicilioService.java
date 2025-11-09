package com.example.PizzUMBurgUM.services;

import com.example.PizzUMBurgUM.entities.*;
import com.example.PizzUMBurgUM.repositories.AdministradorRepository;
import com.example.PizzUMBurgUM.repositories.ClienteRepository;
import com.example.PizzUMBurgUM.repositories.DomicilioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DomicilioService {
    @Autowired
    private DomicilioRepository domicilioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AdministradorRepository administradorRepository;

    public Domicilio crearDomicilioCliente(long clienteId, String numero, String calle, String departamento, String ciudad, String apartamento, boolean predeterminado) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado."));

        Domicilio domicilio = new Domicilio(numero, calle, departamento, ciudad, apartamento, predeterminado, cliente);
        cliente.getDomicilios().add(domicilio);

        return domicilioRepository.save(domicilio);
    }

    public Domicilio crearDomicilioAdministrador(long adminId, String numero, String calle, String departamento, String ciudad, String apartamento) {
        Administrador administrador = administradorRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado."));

        if (administrador.getDomicilioFacturacion() != null) {
            throw new RuntimeException("El administrador ya tiene un domicilio asignado.");
        }

        Domicilio domicilio = new Domicilio(numero, calle, departamento, ciudad, apartamento, false, administrador);
        administrador.setDomicilioFacturacion(domicilio);

        return domicilioRepository.save(domicilio);
    }

    public void eliminarDomicilio(long idDomicilio) {
        domicilioRepository.deleteById(idDomicilio);
    }

    public Domicilio marcarPredeterminado(long clienteId, long idDomicilio) {
        Domicilio dom = buscarPorId(idDomicilio);
        Cliente cliente = dom.getCliente();

        if (cliente.getId() != clienteId) {
           throw new RuntimeException("El domicilio no pertenece al cliente especificado");
        }

        List<Domicilio> domicilios = domicilioRepository.findByClienteId(clienteId);
        for (Domicilio domicilio : domicilios) {
            domicilio.setPredeterminado(domicilio.getId() == idDomicilio);
            domicilioRepository.save(domicilio);
        }
        return domicilioRepository.findById(idDomicilio).orElseThrow(() -> new RuntimeException("Domicilio no encontrado con ID: " + idDomicilio));
    }

    public Domicilio buscarPorId(Long id) {
        return domicilioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Domicilio no encontrado con id: " + id));
    }

    public List<Domicilio> listarPorCliente(Cliente cliente) {
        return domicilioRepository.findByCliente(cliente);
    }
}
