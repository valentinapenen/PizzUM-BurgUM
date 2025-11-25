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
import java.util.Calendar;

@Service
public class TarjetaService {

    @Autowired
    private TarjetaRepository tarjetaRepository;

    @Autowired
    private ClienteRepository clienteRepository;


    public Tarjeta crearTarjeta(String numero, String nombreTitular, long clienteId,
                                TipoTarjeta tipoTarjeta, Date fechaVencimiento, boolean predeterminada) {

        // Validaciones básicas
        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException("El número de la tarjeta es obligatorio.");
        }
        if (nombreTitular == null || nombreTitular.isBlank()) {
            throw new IllegalArgumentException("El nombre del titular es obligatorio.");
        }
        if (tipoTarjeta == null) {
            throw new IllegalArgumentException("El tipo de tarjeta es obligatorio.");
        }
        if (fechaVencimiento == null) {
            throw new IllegalArgumentException("La fecha de vencimiento es obligatoria.");
        }

        // Normalizar fecha al último día del mes (coherente con registro)
        fechaVencimiento = ajustarAlFinDeMes(fechaVencimiento);

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
            // Validar datos requeridos para creación
            if (datos.getNumero() == null || datos.getNumero().isBlank()) {
                throw new IllegalArgumentException("El número de la tarjeta es obligatorio.");
            }
            if (datos.getNombreTitular() == null || datos.getNombreTitular().isBlank()) {
                throw new IllegalArgumentException("El nombre del titular es obligatorio.");
            }
            if (datos.getTipoTarjeta() == null) {
                throw new IllegalArgumentException("El tipo de tarjeta es obligatorio.");
            }
            if (datos.getFecha_vencimiento() == null) {
                throw new IllegalArgumentException("La fecha de vencimiento es obligatoria.");
            }
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

        if (datos.getNumero() == null || datos.getNumero().isBlank()) {
            throw new IllegalArgumentException("El número de la tarjeta es obligatorio.");
        }
        if (datos.getNombreTitular() == null || datos.getNombreTitular().isBlank()) {
            throw new IllegalArgumentException("El nombre del titular es obligatorio.");
        }
        if (datos.getTipoTarjeta() == null) {
            throw new IllegalArgumentException("El tipo de tarjeta es obligatorio.");
        }

        existente.setNumero(datos.getNumero());
        existente.setNombreTitular(datos.getNombreTitular());
        existente.setTipoTarjeta(datos.getTipoTarjeta());

        // Si la fecha de vencimiento viene nula (fallo de binding del formulario),
        // no la sobreescribimos para evitar violar la restricción @NotNull en la BD
        if (datos.getFecha_vencimiento() != null) {
            existente.setFecha_vencimiento(ajustarAlFinDeMes(datos.getFecha_vencimiento()));
        }

        return tarjetaRepository.save(existente);
    }

    /** Ajusta una fecha (yyyy-MM o cualquier día) al último día de ese mes a las 00:00:00 */
    private Date ajustarAlFinDeMes(Date fecha) {
        if (fecha == null) return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        // Ir al último día del mes
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        // Normalizar hora a medianoche
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }


    // elimina tarjeta si pertenece al usuario logeado
    public void eliminarTarjetaDeCliente(long clienteId, long tarjetaId) {
        Tarjeta tarjeta = buscarPorIdDeCliente(clienteId, tarjetaId);
        tarjetaRepository.delete(tarjeta);
    }


}
