package com.example.PizzUMBurgUM.services;


import com.example.PizzUMBurgUM.controllers.DTOS.CreacionAdministradorRequest;
import com.example.PizzUMBurgUM.entities.Administrador;
import com.example.PizzUMBurgUM.entities.Domicilio;
import com.example.PizzUMBurgUM.entities.Usuario;
import com.example.PizzUMBurgUM.repositories.AdministradorRepositorio;
import com.example.PizzUMBurgUM.repositories.UsuarioRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;



@Service
public class AdministradorServicio {

    @Autowired
    private AdministradorRepositorio administradorRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private UsuarioServicio usuarioServicio;


    public Administrador encontrarAdminPorCorreo(String correo) {
        return administradorRepositorio.findById(correo).orElseThrow(() -> new IllegalArgumentException("No existe un administrador con este correo."));
    }



    public Administrador actualizarDatosAdmin(long cedula, Administrador nuevosDatos){

        Administrador admin = administradorRepositorio.findByCedula(cedula);
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
        admin.setDomicilio_facturacion(nuevosDatos.getDomicilio_facturacion());
        return administradorRepositorio.save(admin);

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

        Domicilio domicilio = new Domicilio();
        domicilio.setCalle(creacionAdministradorRequest.getDomicilio_facturacion().getCalle());
        domicilio.setNumero(creacionAdministradorRequest.getDomicilio_facturacion().getNumero());
        domicilio.setDepartamento(creacionAdministradorRequest.getDomicilio_facturacion().getDepartamento());
        domicilio.setCiudad(creacionAdministradorRequest.getDomicilio_facturacion().getCiudad());
        domicilio.setApartamento(creacionAdministradorRequest.getDomicilio_facturacion().getApartamento());
        domicilio.setPredeterminado(true);
        admin.setDomicilio_facturacion(domicilio);
        return usuarioServicio.crearAdministrador(admin);
    }


}
