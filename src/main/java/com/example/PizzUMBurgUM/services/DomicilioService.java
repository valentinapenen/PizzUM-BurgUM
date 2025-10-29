package com.example.PizzUMBurgUM.services;

import com.example.PizzUMBurgUM.entities.Cliente;
import com.example.PizzUMBurgUM.entities.Domicilio;
import com.example.PizzUMBurgUM.entities.Tarjeta;
import com.example.PizzUMBurgUM.repositories.DomicilioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DomicilioService {
    @Autowired
    private DomicilioRepository domicilioRepository;

    //@Autowired
    //private ClienteRepository clienteRepository;

    //public Domicilio agregarDomicilio(String numero, String calle, String departamento, String ciudad, String apartamento, boolean predeterminado, long clienteId) {
    //    Cliente cliente = clienteRepository.findById(clienteId)
    //            .orElseThrow(new RuntimeException("Cliente no encontrado."));
    //    Domicilio domicilio = new Domicilio(numero, calle, departamento, ciudad, apartamento, predeterminado, cliente);
    //}

    public List<Domicilio> listarDomicilioPorCliente(long clienteId) {
        return domicilioRepository.findByClienteId(clienteId);
    }

    public void eliminarDomicilio(long idDomicilio) {
        domicilioRepository.deleteById(idDomicilio);
    }

    public Domicilio marcarPredeterminado(long clienteId, long domicilioId) {
        List<Domicilio> domicilios = domicilioRepository.findByClienteId(clienteId);
        for (Domicilio domicilio : domicilios) {
            domicilio.setPredeterminado(domicilio.getId() == domicilioId);
            domicilioRepository.save(domicilio);
        }
        return domicilioRepository.findById(domicilioId).get();
    }
}
