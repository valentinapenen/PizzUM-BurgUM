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


    // --- NUEVO: helper genérico para guardar cambios en un domicilio ---
    public Domicilio guardar(Domicilio domicilio) {
        return domicilioRepository.save(domicilio);
    }

    // --- NUEVO: métodos "pensados para el cliente logueado" ---

    /** Lista domicilios por id de cliente (por si no querés pasar el objeto Cliente) */
    public List<Domicilio> listarPorClienteId(long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado."));
        return domicilioRepository.findByCliente(cliente);
    }

    /** Obtiene un domicilio que debe pertenecer al cliente; si no, lanza excepción */
    public Domicilio obtenerDomicilioDeCliente(long clienteId, long domicilioId) {

        Domicilio domicilio = buscarPorId(domicilioId);

        if (domicilio.getCliente() == null ||
                domicilio.getCliente().getId() != clienteId) {

            throw new IllegalArgumentException("El domicilio no pertenece al cliente.");
        }

        return domicilio;
    }



    //Elimina un domicilio solo si pertenece al cliente
    public void eliminarDomicilioDeCliente(long clienteId, long domicilioId) {
        Domicilio domicilio = obtenerDomicilioDeCliente(clienteId, domicilioId);
        domicilioRepository.deleteById(domicilio.getId());
    }

    /** Crea o actualiza un domicilio de un cliente según si tiene id o no */
    public Domicilio guardarDomicilioDeCliente(long clienteId, Domicilio datos) {

        // NUEVO domicilio
        if (datos.getId() == 0) {
            return crearDomicilioCliente(
                    clienteId,
                    datos.getNumero(),
                    datos.getCalle(),
                    datos.getDepartamento(),
                    datos.getCiudad(),
                    datos.getApartamento(),
                    false
            );
        }

        // EDITAR existente
        Domicilio existente = obtenerDomicilioDeCliente(clienteId, datos.getId());

        existente.setCalle(datos.getCalle());
        existente.setNumero(datos.getNumero());
        existente.setDepartamento(datos.getDepartamento());
        existente.setCiudad(datos.getCiudad());
        existente.setApartamento(datos.getApartamento());

        return guardar(existente);
    }


}
