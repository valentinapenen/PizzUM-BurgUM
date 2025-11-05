package com.example.PizzUMBurgUM.services;


import com.example.PizzUMBurgUM.controllers.DTOS.RegistroClienteRequest;
import com.example.PizzUMBurgUM.entities.Cliente;
import com.example.PizzUMBurgUM.entities.Domicilio;
import com.example.PizzUMBurgUM.entities.Tarjeta;
import com.example.PizzUMBurgUM.repositories.ClienteRepositorio;
import com.example.PizzUMBurgUM.repositories.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private DomicilioService domicilioService;
    @Autowired
    private TarjetaService tarjetaService;


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



        Cliente clienteGuardado = usuarioServicio.registrarCliente(cliente);


        domicilioService.crearDomicilio(clienteGuardado.getCorreo(),
                registroClienteRequest.getDomicilio().getNumero(),
                registroClienteRequest.getDomicilio().getCalle(),
                registroClienteRequest.getDomicilio().getDepartamento(),
                registroClienteRequest.getDomicilio().getCiudad(),
                registroClienteRequest.getDomicilio().getApartamento(),
                true);

        tarjetaService.crearTarjeta(registroClienteRequest.getTarjeta().getNumero(),
                registroClienteRequest.getTarjeta().getNombreTitular(),
                clienteGuardado.getCorreo(), registroClienteRequest.getTarjeta().getTipoTarjeta(),
                registroClienteRequest.getTarjeta().getFechaVencimiento(), true);



        return clienteGuardado;
        
    }


    public Cliente actualizarCliente(String correoCliente,  Cliente nuevosDatos){

        Cliente cliente = clienteRepositorio.findById(correoCliente).orElse(null);

        if(cliente == null){
            throw new IllegalArgumentException("No existe un administrador con este correo.");
        }

        if (!nuevosDatos.getCedula().equals(correoCliente)){
            throw new IllegalArgumentException("No se puede cambiar la cedula.");
        }
        if (!nuevosDatos.getCorreo().equals(cliente.getCorreo())){
            throw new IllegalArgumentException("No se puede cambiar el correo.");
        }

        cliente.setNombre(nuevosDatos.getNombre());
        cliente.setApellido(nuevosDatos.getApellido());
        cliente.setFechaNacimiento(nuevosDatos.getFechaNacimiento());
        cliente.setTelefono(nuevosDatos.getTelefono());
        cliente.setContrasena(nuevosDatos.getContrasena());

        return clienteRepositorio.save(cliente);
    }

    public Cliente buscarPorId(String correoCliente){
        return clienteRepositorio.findById(correoCliente).orElseThrow(() -> new RuntimeException("Cliente no encontrado."));
    }

}
