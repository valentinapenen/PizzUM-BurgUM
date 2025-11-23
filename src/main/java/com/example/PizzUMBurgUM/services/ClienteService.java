package com.example.PizzUMBurgUM.services;


import com.example.PizzUMBurgUM.controllers.DTOS.RegistroClienteRequest;
import com.example.PizzUMBurgUM.entities.Cliente;
import com.example.PizzUMBurgUM.repositories.ClienteRepository;
import com.example.PizzUMBurgUM.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private DomicilioService domicilioService;
    @Autowired
    private TarjetaService tarjetaService;

    public void verificarDatosCliente( Cliente cliente){

        if (cliente.getNombre() == null || cliente.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }

        if (cliente.getApellido() == null || cliente.getApellido().isBlank()) {
            throw new IllegalArgumentException("El apellido es obligatorio.");
        }

        if (cliente.getCedula() == null){
            throw new IllegalArgumentException("La cedula es obligatoria.");
        }

        if (cliente.getFechaNacimiento() == null){
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria.");
        }

        if(cliente.getCorreo() ==  null || cliente.getCorreo().isBlank()){
            throw new IllegalArgumentException("El correo es obligatorio.");
        }

        if (cliente.getTelefono() == null || cliente.getTelefono().isBlank()){
            throw new IllegalArgumentException("El teléfono es obligatorio.");
        }

        if (cliente.getContrasena() == null || cliente.getContrasena().isBlank()){
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }

        if(usuarioRepository.existsByCorreo(cliente.getCorreo())){
            throw new IllegalArgumentException("El correo ya está registrado en el sistema.");
        }

        if(clienteRepository.existsByCedula(cliente.getCedula())){
            throw new  IllegalArgumentException("Ya existe un cliente con esta cédula.");
        }


    }

    public Cliente registrarCliente(RegistroClienteRequest registroClienteRequest){
        if (registroClienteRequest.getDomicilio() == null){
            throw new IllegalArgumentException("Debe de ingresar al menos un domicilio.");
        }
        if (registroClienteRequest.getTarjeta() == null){
            throw new IllegalArgumentException("Debe de ingresar al menos una tarjeta.");
        }

        Cliente cliente = new Cliente();
        cliente.setNombre(registroClienteRequest.getNombre());
        cliente.setApellido(registroClienteRequest.getApellido());
        cliente.setCedula(registroClienteRequest.getCedula());
        cliente.setFechaNacimiento(registroClienteRequest.getFechaNacimiento());
        cliente.setCorreo(registroClienteRequest.getCorreo());
        cliente.setTelefono(registroClienteRequest.getTelefono());
        cliente.setContrasena(registroClienteRequest.getContrasena());

        verificarDatosCliente(cliente);

        Cliente clienteGuardado = clienteRepository.save(cliente);

                domicilioService.crearDomicilioCliente(
                clienteGuardado.getId(),
                registroClienteRequest.getDomicilio().getNumero(),
                registroClienteRequest.getDomicilio().getCalle(),
                registroClienteRequest.getDomicilio().getDepartamento(),
                registroClienteRequest.getDomicilio().getCiudad(),
                registroClienteRequest.getDomicilio().getApartamento(),
                true);

        tarjetaService.crearTarjeta(
                registroClienteRequest.getTarjeta().getNumero(),
                registroClienteRequest.getTarjeta().getNombreTitular(),
                clienteGuardado.getId(),
                registroClienteRequest.getTarjeta().getTipoTarjeta(),
                registroClienteRequest.getTarjeta().getFechaVencimiento(),
                true);

        return clienteGuardado;
    }


    public Cliente actualizarCliente(long idCliente,  Cliente nuevosDatos){

        Cliente cliente = clienteRepository.findById(idCliente).orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + idCliente));

        if(cliente == null){
            throw new IllegalArgumentException("No existe este cliente.");
        }

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

        if(nuevosDatos.getFechaNacimiento() != null ){
            cliente.setFechaNacimiento(nuevosDatos.getFechaNacimiento());
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
