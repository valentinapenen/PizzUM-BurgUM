package com.example.PizzUMBurgUM.services;


import com.example.PizzUMBurgUM.entities.Administrador;
import com.example.PizzUMBurgUM.entities.Cliente;
import com.example.PizzUMBurgUM.entities.Usuario;
import com.example.PizzUMBurgUM.repositories.AdministradorRepositorio;
import com.example.PizzUMBurgUM.repositories.ClienteRepositorio;
import com.example.PizzUMBurgUM.repositories.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServicio {

    @Autowired
    private AdministradorRepositorio administradorRepositorio;
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    public Usuario login(String correo, String password){

        Administrador admin = administradorRepositorio.findById(correo).orElse(null);

        if(admin != null){
            if(admin.getContrasena().equals(password)){
                return admin;
            }
            else throw new IllegalArgumentException("Contraseña incorrecta.");
        }

        Cliente cliente = clienteRepositorio.findById(correo).orElse(null);

        if (cliente != null){
            if  (cliente.getContrasena().equals(password)){
                return cliente;
            }
            else throw new IllegalArgumentException("Contraseña incorrecta.");
        }

        throw new  IllegalArgumentException("No existe un usuario con este correo.");
    }



    public boolean correoEnUso(String correo){
        if (usuarioRepositorio.existsByCorreo(correo)){
            return true;
        }
        else return false;
    }


    public Cliente registrarCliente(Cliente cliente){

        if (cliente.getNombre() == null || cliente.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }

        if (cliente.getApellido() == null || cliente.getApellido().isBlank()) {
            throw new IllegalArgumentException("El apellido es obligatorio.");
        }

        if (cliente.getCedula() == null){
            throw new IllegalArgumentException("La cedula es obligatorio.");
        }

        if (cliente.getFechaNacimiento() == null){
            throw new IllegalArgumentException("La fecha de nacimiento es obligatorio.");
        }

        if(cliente.getCorreo() ==  null || cliente.getCorreo().isBlank()){
            throw new IllegalArgumentException("El correo es obligatorio.");
        }

        if (cliente.getTelefono() == null || cliente.getTelefono().isBlank()){
            throw new IllegalArgumentException("El teléfono es obligatorio.");
        }

        if (cliente.getContrasena() == null || cliente.getContrasena().isBlank()){
            throw new IllegalArgumentException("La contrasena es obligatorio.");
        }

        if(cliente.getDomicilios() == null || cliente.getDomicilios().isEmpty()){
            throw new IllegalArgumentException("El agregar un domicilio es obligatorio.");
        }

        if(cliente.getTarjetas() == null || cliente.getTarjetas().isEmpty()){
            throw new IllegalArgumentException("El agregar una tarjeta es obligatorio.");
        }

        if(usuarioRepositorio.existsByCorreo(cliente.getCorreo())){
            throw new IllegalArgumentException("El correo ya está registrado en el sistema.");
        }

        if(clienteRepositorio.existsByCedula(cliente.getCedula())){
            throw new  IllegalArgumentException("Ya existe un cliente con esta cédula.");
        }

        return clienteRepositorio.save(cliente);
    }


    public Administrador crearAdministrador(Administrador admin) {

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

        if (admin.getDomicilio_facturacion() == null){
            throw new IllegalArgumentException("El domicilio de facturación es obligatorio.");
        }

        if(usuarioRepositorio.existsByCorreo(admin.getCorreo())){
            throw new IllegalArgumentException("El correo ya está registrado en el sistema.");
        }

        if(administradorRepositorio.existsByCedula(admin.getCedula())){
            throw new IllegalArgumentException("Ya existe un administrador con esta cédula.");
        }

        return administradorRepositorio.save(admin);
    }



    // Falta agregar lo de la actualizacion de datos de los usuarios (preguntar si los admins pueden actualizar sus datos o no)

}
