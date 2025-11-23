package com.example.PizzUMBurgUM.services;

import com.example.PizzUMBurgUM.entities.Administrador;
import com.example.PizzUMBurgUM.entities.Cliente;
import com.example.PizzUMBurgUM.entities.Usuario;
import com.example.PizzUMBurgUM.repositories.AdministradorRepository;
import com.example.PizzUMBurgUM.repositories.ClienteRepository;
import com.example.PizzUMBurgUM.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServicio {
    @Autowired
    private AdministradorRepository administradorRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario login(String correo, String password){
        Administrador admin = administradorRepository.findByCorreo(correo); //Saque el orElse(null) porque si no lo encuentra ya retorna null
        if(admin != null){
            if(admin.getContrasena().equals(password)){
                return admin;
            }
            else throw new IllegalArgumentException("Contraseña incorrecta.");
        }

        Cliente cliente = clienteRepository.findByCorreo(correo);  //Saque el orElse(null) porque si no lo encuentra ya retorna null
        if (cliente != null){
            if  (cliente.getContrasena().equals(password)){
                return cliente;
            }
            else throw new IllegalArgumentException("Contraseña incorrecta.");
        }

        throw new  IllegalArgumentException("No existe un usuario con este correo.");
    }

    public boolean correoEnUso(String correo){
        if (usuarioRepository.existsByCorreo(correo)){
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

        return clienteRepository.save(cliente);
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

        if (admin.getDomicilioFacturacion() == null){
            throw new IllegalArgumentException("El domicilio de facturación es obligatorio.");
        }

        if(usuarioRepository.existsByCorreo(admin.getCorreo())){
            throw new IllegalArgumentException("El correo ya está registrado en el sistema.");
        }

        if(administradorRepository.existsByCedula(admin.getCedula())){
            throw new IllegalArgumentException("Ya existe un administrador con esta cédula.");
        }

        return administradorRepository.save(admin);
    }

    public long contarAdministradores() {
        return administradorRepository.count();
    }

    public long contarClientes() {
        return clienteRepository.count();
    }

    public long contarUsuarios() {
        return administradorRepository.count() + clienteRepository.count();
    }




    // Falta agregar lo de la actualizacion de datos de los usuarios (preguntar si los admins pueden actualizar sus datos o no)
}
