package com.example.PizzUMBurgUM.services;

import com.example.PizzUMBurgUM.entities.*;
import com.example.PizzUMBurgUM.repositories.AdministradorRepositorio;
import com.example.PizzUMBurgUM.repositories.ClienteRepositorio;
import com.example.PizzUMBurgUM.repositories.DomicilioRepository;
import com.example.PizzUMBurgUM.repositories.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DomicilioService {
    @Autowired
    private DomicilioRepository domicilioRepository;

    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private AdministradorRepositorio administradorRepositorio;

    public Domicilio crearDomicilio(String clienteId, String numero, String calle, String departamento, String ciudad, String apartamento, boolean predeterminado) {
        Cliente cliente = clienteRepositorio.findById(clienteId)
                .orElseThrow(() ->new RuntimeException("Cliente no encontrado."));
        Domicilio domicilio = new Domicilio(numero, calle, departamento, ciudad, apartamento, predeterminado);
        cliente.getDomicilios().add(domicilio);
        domicilio.setCliente(cliente);
        return domicilioRepository.save(domicilio);
    }

    public void eliminarDomicilio(long idDomicilio) {
        domicilioRepository.deleteById(idDomicilio);
    }

    public Domicilio marcarPredeterminado(String clienteId, long domicilioId) {
        Domicilio dom = buscarPorId(domicilioId);
        Cliente cliente = dom.getCliente();

        if (cliente.getCorreo() != clienteId) {
           throw new RuntimeException("El domicilio no pertenece al cliente especificado");
        }

        List<Domicilio> domicilios = domicilioRepository.findByClienteId(clienteId);
        for (Domicilio domicilio : domicilios) {
            domicilio.setPredeterminado(domicilio.getId() == domicilioId);
            domicilioRepository.save(domicilio);
        }
        return domicilioRepository.findById(domicilioId).get();
    }

    public Domicilio buscarPorId(Long id) {
        return domicilioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Domicilio no encontrado con id: " + id));
    }

    public List<Domicilio> listarPorCliente(Cliente cliente) {
        return domicilioRepository.findByCliente(cliente);
    }
}
