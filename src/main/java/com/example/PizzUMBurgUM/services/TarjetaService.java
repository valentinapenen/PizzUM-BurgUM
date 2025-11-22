package com.example.PizzUMBurgUM.services;

import com.example.PizzUMBurgUM.entities.Cliente;
import com.example.PizzUMBurgUM.entities.Tarjeta;
import com.example.PizzUMBurgUM.entities.enums.TipoTarjeta;
import com.example.PizzUMBurgUM.repositories.ClienteRepository;
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
    private ClienteRepository clienteRepository;

    // Crear tarjeta (ya lo tenías)
    public Tarjeta crearTarjeta(String numero, String nombreTitular, long clienteId,
                                TipoTarjeta tipoTarjeta, Date fechaVencimiento, boolean predeterminada) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado."));

        Tarjeta tarjeta = Tarjeta.builder()
                .numero(numero)
                .nombreTitular(nombreTitular)
                .cliente(cliente)
                .tipoTarjeta(tipoTarjeta)
                .fecha_vencimiento(fechaVencimiento)
                .predeterminada(predeterminada)
                .build();

        cliente.getTarjetas().add(tarjeta);
        return tarjetaRepository.save(tarjeta);
    }

    // Listar tarjetas por cliente (ya lo tenías)
    public List<Tarjeta> listarTarjetasPorCliente(long clienteId) {
        return tarjetaRepository.findByClienteId(clienteId);
    }

    // Eliminar tarjeta (ya lo tenías)
    public void eliminarTarjeta(long tarjetaId) {
        tarjetaRepository.deleteById(tarjetaId);
    }

    // Marcar una tarjeta como predeterminada (ya lo tenías)
    public Tarjeta marcarPredeterminada(long clienteId, long tarjetaId) {
        List<Tarjeta> tarjetas = tarjetaRepository.findByClienteId(clienteId);

        for (Tarjeta tarjeta : tarjetas) {
            tarjeta.setPredeterminada(tarjeta.getId() == tarjetaId);
            tarjetaRepository.save(tarjeta);
        }

        return tarjetaRepository.findById(tarjetaId).orElse(null);
    }

    //Buscar tarjeta por número (para el webservice REST)
    public Tarjeta buscarTarjetaPorNumero(String numero) {
        return tarjetaRepository.findByNumero(numero);
    }
}
