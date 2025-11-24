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


    public List<Tarjeta> listarTarjetasPorCliente(long clienteId) {
        return tarjetaRepository.findByClienteId(clienteId);
    }


    public void eliminarTarjeta(long tarjetaId) {
        tarjetaRepository.deleteById(tarjetaId);
    }


    public Tarjeta marcarPredeterminada(long clienteId, long tarjetaId) {
        List<Tarjeta> tarjetas = tarjetaRepository.findByClienteId(clienteId);

        for (Tarjeta tarjeta : tarjetas) {
            tarjeta.setPredeterminada(tarjeta.getId() == tarjetaId);
            tarjetaRepository.save(tarjeta);
        }

        return tarjetaRepository.findById(tarjetaId).orElse(null);
    }


    public Tarjeta buscarTarjetaPorNumero(String numero) {
        return tarjetaRepository.findByNumero(numero);
    }


    public Tarjeta buscarPorId(long id) {
        return tarjetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada."));
    }

    /** Obtiene una tarjeta y verifica que pertenezca al cliente */
    public Tarjeta buscarPorIdDeCliente(long clienteId, long tarjetaId) {
        Tarjeta t = buscarPorId(tarjetaId);

        if (t.getCliente().getId() != clienteId) {
            throw new IllegalArgumentException("La tarjeta no pertenece al cliente.");
        }

        return t;
    }

    /** Crea o edita una tarjeta del cliente */
    public Tarjeta guardarTarjetaDeCliente(long clienteId, Tarjeta datos) {

        // NUEVA tarjeta
        if (datos.getId() == 0) {
            return crearTarjeta(
                    datos.getNumero(),
                    datos.getNombreTitular(),
                    clienteId,
                    datos.getTipoTarjeta(),
                    datos.getFecha_vencimiento(),
                    false  // no predeterminada por defecto
            );
        }

        // EDITAR tarjeta existente
        Tarjeta existente = buscarPorIdDeCliente(clienteId, datos.getId());

        existente.setNumero(datos.getNumero());
        existente.setNombreTitular(datos.getNombreTitular());
        existente.setTipoTarjeta(datos.getTipoTarjeta());
        existente.setFecha_vencimiento(datos.getFecha_vencimiento());

        return tarjetaRepository.save(existente);
    }


    // elimina tarjeta si pertenece al usuario logeado
    public void eliminarTarjetaDeCliente(long clienteId, long tarjetaId) {
        Tarjeta tarjeta = buscarPorIdDeCliente(clienteId, tarjetaId);
        tarjetaRepository.delete(tarjeta);
    }


}
