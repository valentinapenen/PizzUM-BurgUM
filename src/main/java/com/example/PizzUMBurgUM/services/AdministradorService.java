package com.example.PizzUMBurgUM.services;


import com.example.PizzUMBurgUM.controllers.DTOS.CreacionAdministradorRequest;
import com.example.PizzUMBurgUM.entities.Administrador;
import com.example.PizzUMBurgUM.entities.Domicilio;
import com.example.PizzUMBurgUM.repositories.AdministradorRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Service
public class AdministradorService {

    @Autowired
    private AdministradorRepository administradorRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private DomicilioService domicilioService;

    public List<Administrador> listarTodos() {
        List<Administrador> administradores = administradorRepository.findAll();
        return administradores;
    }

    public Administrador encontrarAdminPorCorreo(String adminCorreo) {
        return administradorRepository.findByCorreo(adminCorreo);
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

        if(nuevosDatos.getNombre() != null && !nuevosDatos.getNombre().isBlank()){
            admin.setNombre(nuevosDatos.getNombre());
        }

        if(nuevosDatos.getApellido() != null && !nuevosDatos.getApellido().isBlank()){
            admin.setApellido(nuevosDatos.getApellido());
        }

        if(nuevosDatos.getFechaNacimiento() != null ){
            admin.setFechaNacimiento(nuevosDatos.getFechaNacimiento());
        }

        if(nuevosDatos.getTelefono() != null && !nuevosDatos.getTelefono().isBlank()){
            admin.setTelefono(nuevosDatos.getTelefono());
        }

        if(nuevosDatos.getContrasena() != null && !nuevosDatos.getContrasena().isBlank()){
            admin.setContrasena(nuevosDatos.getContrasena());
        }

        if (nuevosDatos.getDomicilioFacturacion() != null){

            Domicilio domicilioNuevo = nuevosDatos.getDomicilioFacturacion();
            Domicilio domicilioActual = admin.getDomicilioFacturacion();

            if (domicilioNuevo.getNumero() != null) domicilioActual.setNumero(domicilioNuevo.getNumero());
            if (domicilioNuevo.getCalle() != null && !domicilioNuevo.getCalle().isBlank()) domicilioActual.setCalle(domicilioNuevo.getCalle());
            if (domicilioNuevo.getDepartamento() != null && !domicilioNuevo.getDepartamento().isBlank()) domicilioActual.setDepartamento(domicilioNuevo.getDepartamento());
            if (domicilioNuevo.getCiudad() != null && !domicilioNuevo.getCiudad().isBlank()) domicilioActual.setCiudad(domicilioNuevo.getCiudad());
            if (domicilioNuevo.getApartamento() != null && !domicilioNuevo.getApartamento().isBlank()) domicilioActual.setApartamento(domicilioNuevo.getApartamento());

            }
        return administradorRepository.save(admin);

    }

    public void verificarDatosAdmin(Administrador admin){
        if (admin.getNombre() == null || admin.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }

        if (admin.getApellido() == null || admin.getApellido().isBlank()) {
            throw new IllegalArgumentException("El apellido es obligatorio.");
        }

        if (admin.getCedula() == null){
            throw new IllegalArgumentException("La cedula es obligatorio.");
        }

        if (admin.getFechaNacimiento() == null){
            throw new IllegalArgumentException("La fecha de nacimiento es obligatorio.");
        }

        if(admin.getCorreo() ==  null || admin.getCorreo().isBlank()){
            throw new IllegalArgumentException("El correo es obligatorio.");
        }

        if (admin.getTelefono() == null || admin.getTelefono().isBlank()){
            throw new IllegalArgumentException("El teléfono es obligatorio.");
        }

        if (admin.getContrasena() == null || admin.getContrasena().isBlank()){
            throw new IllegalArgumentException("La contrasena es obligatorio.");
        }

        if (admin.getDomicilioFacturacion() == null){
            throw new IllegalArgumentException("El domicilio de facturación es obligatorio.");
        }

        if(usuarioService.correoEnUso(admin.getCorreo())){
            throw new IllegalArgumentException("El correo ya está registrado en el sistema.");
        }

        if(administradorRepository.existsByCedula(admin.getCedula())){
            throw new IllegalArgumentException("Ya existe un administrador con esta cédula.");
        }


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
                true,
                admin);
        domicilio.setCliente(null);
        admin.setDomicilioFacturacion(domicilio);
        verificarDatosAdmin(admin);

        return administradorRepository.save(admin);
    }

}
