package com.example.PizzUMBurgUM.services;


import com.example.PizzUMBurgUM.entities.Administrador;
import com.example.PizzUMBurgUM.repositories.AdministradorRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;



@Service
public class AdministradorServicio {

    @Autowired
    private AdministradorRepositorio administradorRepositorio;


    public Administrador guardarAdministrador(Administrador admin) {

        if (admin.getNombre() == null || admin.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        if (admin.getApellido() == null || admin.getApellido().isBlank()) {
            throw new IllegalArgumentException("El apellido es obligatorio");
        }

        if (admin.getCedula() == null){
            throw new IllegalArgumentException("La cedula es obligatorio");
        }

        if (admin.getFechaNacimiento() == null){
            throw new IllegalArgumentException("La fecha de nacimiento es obligatorio");
        }

        if(admin.getCorreo() ==  null || admin.getCorreo().isBlank()){
            throw new IllegalArgumentException("El correo es obligatorio");
        }

        if (admin.getTelefono() == null || admin.getTelefono().isBlank()){
            throw new IllegalArgumentException("El teléfono es obligatorio");
        }

        if (admin.getContrasena() == null || admin.getContrasena().isBlank()){
            throw new IllegalArgumentException("La contrasena es obligatorio");
        }

        if (admin.getDomicilio_facturacion() == null){
            throw new IllegalArgumentException("El domicilio de facturación es obligatorio");
        }

        Administrador AdminYaExistenteCorreo = administradorRepositorio.findByCorreo(admin.getCorreo());
        if (AdminYaExistenteCorreo != null) {
            throw new IllegalArgumentException("Este correo ya está en uso, ingrese otro.");
        }

        if (administradorRepositorio.findById(admin.getCedula()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un administrador con este correo, ingrese otro.");
        }

        return administradorRepositorio.save(admin);

    }


    public Administrador encontrarAdminPorCorreo(String correo) {
        return administradorRepositorio.findByCorreo(correo);
    }


    public Administrador loginAdministrador(String correo, String contrasena) {

        Administrador admin = administradorRepositorio.findByCorreo(correo);

        if (admin == null){
            throw new IllegalArgumentException("No existe un administrador con el correo ingresado");
        }

        if (!admin.getContrasena().equals(contrasena)) {
            throw new IllegalArgumentException("La contraseña es incorrecta.");
        }

        return admin;
    }

    public Administrador actualizarDatosAdmin(long cedula, Administrador NuevosDatos){

        Administrador admin = administradorRepositorio.findById(cedula).orElseThrow(() -> new IllegalArgumentException("No existe un administrador con la cedula ingresada."));

        Administrador MismoCorreo = administradorRepositorio.findByCorreo(NuevosDatos.getCorreo());
        if (MismoCorreo != null) {
            throw new IllegalArgumentException("El correo ingresado ya esta siendo utilizado.");
        }

        admin.setNombre(NuevosDatos.getNombre());
        admin.setApellido(NuevosDatos.getApellido());
        admin.setCedula(NuevosDatos.getCedula());
        admin.setFechaNacimiento(NuevosDatos.getFechaNacimiento());
        admin.setCorreo(NuevosDatos.getCorreo());
        admin.setTelefono(NuevosDatos.getTelefono());
        admin.setContrasena(NuevosDatos.getContrasena());
        admin.setDomicilio_facturacion(NuevosDatos.getDomicilio_facturacion());
        return administradorRepositorio.save(admin);

    }


}
