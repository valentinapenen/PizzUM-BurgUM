package com.example.PizzUMBurgUM.services;

import com.example.PizzUMBurgUM.entities.Cliente;
import com.example.PizzUMBurgUM.entities.Tarjeta;
import com.example.PizzUMBurgUM.entities.enums.TipoTarjeta;
import com.example.PizzUMBurgUM.repositories.ClienteRepositorio;
import com.example.PizzUMBurgUM.repositories.TarjetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TarjetaService {
    @Autowired
    private TarjetaRepository tarjetaRepository;
    @Autowired
    private ClienteRepositorio clienteRepository;

    public Tarjeta crearTarjeta(String numero, String nombreTitular, String clienteId, TipoTarjeta tipoTarjeta, Date fechaVencimiento, boolean predeterminada) {
        Cliente cliente = clienteRepository.findById(clienteId)
               .orElseThrow(() -> new RuntimeException("Cliente no encontrado."));

        Tarjeta tarjeta = new Tarjeta(Tarjeta.enmascarar(numero), nombreTitular, cliente, tipoTarjeta, fechaVencimiento, predeterminada);
        cliente.getTarjetas().add(tarjeta);
        return tarjetaRepository.save(tarjeta);
    }

    public List<Tarjeta> listarTarjetasPorCliente(String clienteId) {
        return tarjetaRepository.findByClienteId(clienteId);
    }

    public void eliminarTarjeta(long tarjetaId) {
        tarjetaRepository.deleteById(tarjetaId);
    }

    public Tarjeta marcarPredeterminada (String clienteId, long tarjetaId) {
        List<Tarjeta> tarjetas = tarjetaRepository.findByClienteId(clienteId);
        for (Tarjeta tarjeta : tarjetas) {
            tarjeta.setPredeterminada(tarjeta.getId() == tarjetaId);
            tarjetaRepository.save(tarjeta);
        }
        return tarjetaRepository.findById(tarjetaId).get();
    }
}
