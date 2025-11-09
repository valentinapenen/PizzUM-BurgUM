package com.example.PizzUMBurgUM.services;


import com.example.PizzUMBurgUM.controllers.DTOS.CreacionAdministradorRequest;
import com.example.PizzUMBurgUM.entities.Administrador;
import com.example.PizzUMBurgUM.entities.Domicilio;
import com.example.PizzUMBurgUM.repositories.AdministradorRepository;
import com.example.PizzUMBurgUM.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;



@Service
public class AdministradorService {

    @Autowired
    private AdministradorRepository administradorRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private DomicilioService domicilioService;


    public Administrador encontrarAdminPorCorreo(long adminId) {
        return administradorRepository.findById(adminId).orElseThrow(() -> new IllegalArgumentException("No existe un administrador con este correo."));
    }



    public Administrador actualizarDatosAdmin(String cedula, Administrador nuevosDatos){

        Administrador admin = administradorRepository.findByCedula(cedula);
        if(admin == null){
            throw new IllegalArgumentException("No existe un administrador con este cedula.");
        }

        if (!nuevosDatos.getCedula().equals(cedula)){
            throw new IllegalArgumentException("No se puede cambiar la cedula.");
        }
        if (!nuevosDatos.getCorreo().equals(admin.getCorreo())){
            throw new IllegalArgumentException("No se puede cambiar el correo.");
        }
        //if (!nuevosDatos.getDomicilio_facturacion().equals(admin.getDomicilio_facturacion())){
        //    throw new IllegalArgumentException("No se puede cambiar el domicilio de facturación.");
        //}

        admin.setNombre(nuevosDatos.getNombre());
        admin.setApellido(nuevosDatos.getApellido());
        admin.setFechaNacimiento(nuevosDatos.getFechaNacimiento());
        admin.setTelefono(nuevosDatos.getTelefono());
        admin.setContrasena(nuevosDatos.getContrasena());
        admin.setDomicilioFacturacion(nuevosDatos.getDomicilioFacturacion());
        return administradorRepository.save(admin);

    }


    public Administrador crearAdministrador(CreacionAdministradorRequest creacionAdministradorRequest){
        Administrador admin = new Administrador();
        admin.setNombre(creacionAdministradorRequest.getNombre());
        admin.setApellido(creacionAdministradorRequest.getApellido());
        admin.setCedula(creacionAdministradorRequest.getCedula());
        admin.setFechaNacimiento(creacionAdministradorRequest.getFechaNacimiento());
        admin.setCorreo(creacionAdministradorRequest.getCorreo());
        admin.setTelefono(creacionAdministradorRequest.getTelefono());
        admin.setContrasena(creacionAdministradorRequest.getContrasena());

        Domicilio domicilio = new Domicilio(
                creacionAdministradorRequest.getDomicilio_facturacion().getNumero(),
                creacionAdministradorRequest.getDomicilio_facturacion().getCalle(),
                creacionAdministradorRequest.getDomicilio_facturacion().getDepartamento(),
                creacionAdministradorRequest.getDomicilio_facturacion().getCiudad(),
                creacionAdministradorRequest.getDomicilio_facturacion().getApartamento(),
                false,
                admin);
        domicilio.setCliente(null);
        admin.setDomicilioFacturacion(domicilio);
        return usuarioServicio.crearAdministrador(admin);
    }


}
