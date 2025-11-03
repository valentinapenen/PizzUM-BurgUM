package com.example.PizzUMBurgUM.services;


import com.example.PizzUMBurgUM.entities.Administrador;
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
    


    public Administrador encontrarAdminPorCorreo(String correo) {
        return administradorRepositorio.findById(correo).orElseThrow(() -> new IllegalArgumentException("No existe un administrador con este correo."));
    }



    public Administrador actualizarDatosAdmin(long cedula, Administrador NuevosDatos){

        Administrador admin = administradorRepositorio.findByCedula(cedula);

        Usuario MismoCorreo = usuarioRepositorio.findById(NuevosDatos.getCorreo()).orElse(null);
        if (MismoCorreo != null) {
            throw new IllegalArgumentException("El correo ingresado ya esta siendo utilizado por otro usuario.");
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
