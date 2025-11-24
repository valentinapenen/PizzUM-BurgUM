package com.example.PizzUMBurgUM.services;


import com.example.PizzUMBurgUM.controllers.DTOS.RegistroClienteRequest;
import com.example.PizzUMBurgUM.entities.Cliente;
import com.example.PizzUMBurgUM.entities.Domicilio;
import com.example.PizzUMBurgUM.entities.Tarjeta;
import com.example.PizzUMBurgUM.repositories.ClienteRepository;
import com.example.PizzUMBurgUM.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;

@Service
public class ClienteService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Cliente registrarCliente(RegistroClienteRequest req){
        if (!req.getContrasena().equals(req.getContrasena2())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden.");
        }

        if (usuarioRepository.existsByCorreo(req.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está registrado.");
        }

        if (clienteRepository.existsByCedula(req.getCedula())) {
            throw new IllegalArgumentException("Ya existe un cliente con esta cédula.");
        }

        Cliente cliente = Cliente.builder()
                .nombre(req.getNombre())
                .apellido(req.getApellido())
                .cedula(req.getCedula())
                .fechaNacimiento(req.getFechaNacimiento())
                .correo(req.getCorreo())
                .telefono(req.getTelefono())
                .contrasena(passwordEncoder.encode(req.getContrasena()))
                .domicilios(new ArrayList<>())
                .tarjetas(new ArrayList<>())
                .pedidos(new ArrayList<>())
                .build();

        Domicilio domicilio = Domicilio.builder()
                .numero(req.getDomicilio().getNumero())
                .calle(req.getDomicilio().getCalle())
                .departamento(req.getDomicilio().getDepartamento())
                .ciudad(req.getDomicilio().getCiudad())
                .apartamento(req.getDomicilio().getApartamento())
                .predeterminado(true)
                .cliente(cliente)
                .administrador(null)
                .build();

        cliente.getDomicilios().add(domicilio);

        // Convertir el string de fecha (YYYY-MM) a un objeto Date
        Date fechaVencimiento = null;
        try {
            String fechaStr = req.getTarjeta().getFechaVencimiento();
            // Añadir el día (último día del mes)
            String[] partes = fechaStr.split("-");
            int año = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);

            Calendar calendar = Calendar.getInstance();
            calendar.set(año, mes - 1, 1); // Mes en Calendar es 0-based
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

            fechaVencimiento = calendar.getTime();
        } catch (Exception e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use YYYY-MM.");
        }

        Tarjeta tarjeta = Tarjeta.builder()
                .numero(req.getTarjeta().getNumero())
                .nombreTitular(req.getTarjeta().getNombreTitular())
                .tipoTarjeta(req.getTarjeta().getTipoTarjeta())
                .fecha_vencimiento(fechaVencimiento)
                .predeterminada(true)
                .cliente(cliente)
                .build();

        cliente.getTarjetas().add(tarjeta);

        return clienteRepository.save(cliente);
    }


    public Cliente actualizarCliente(long idCliente,  Cliente nuevosDatos){

        Cliente cliente = clienteRepository.findById(idCliente).orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + idCliente));

        // Validaciones de campos no modificables
        if (!nuevosDatos.getCedula().equals(cliente.getCedula())){
            throw new IllegalArgumentException("No se puede cambiar la cedula.");
        }
        if (!nuevosDatos.getCorreo().equals(cliente.getCorreo())){
            throw new IllegalArgumentException("No se puede cambiar el correo.");
        }

        if(nuevosDatos.getNombre() != null && !nuevosDatos.getNombre().isBlank()){
            cliente.setNombre(nuevosDatos.getNombre());
        }

        if(nuevosDatos.getApellido() != null && !nuevosDatos.getApellido().isBlank()){
            cliente.setApellido(nuevosDatos.getApellido());
        }

        if(nuevosDatos.getTelefono() != null && !nuevosDatos.getTelefono().isBlank()){
            cliente.setTelefono(nuevosDatos.getTelefono());
        }

        if(nuevosDatos.getContrasena() != null && !nuevosDatos.getContrasena().isBlank()){
            cliente.setContrasena(nuevosDatos.getContrasena());
        }

        return clienteRepository.save(cliente);
    }

    public Cliente buscarPorId(long clienteId){
        return clienteRepository.findById(clienteId).orElseThrow(() -> new RuntimeException("Cliente no encontrado."));
    }


}
