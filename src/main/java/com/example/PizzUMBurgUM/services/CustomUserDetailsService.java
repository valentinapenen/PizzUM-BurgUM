package com.example.PizzUMBurgUM.services;

import com.example.PizzUMBurgUM.entities.Usuario;
import com.example.PizzUMBurgUM.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        // Log para depuración
        System.out.println("Intentando autenticar usuario con correo: " + correo);

        // Buscar el usuario por correo
        Usuario usuario = usuarioRepository.findByCorreo(correo);

        if (usuario == null) {
            System.out.println("Usuario no encontrado con correo: " + correo);
            throw new UsernameNotFoundException("Usuario no encontrado con correo: " + correo);
        }

        System.out.println("Usuario encontrado: " + usuario.getNombre() + " " + usuario.getApellido());
        System.out.println("ID del usuario: " + usuario.getId());
        System.out.println("Contraseña codificada: " + usuario.getContrasena());

        // Determinar el rol basado en el tipo de usuario
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if (usuario.getClass().getSimpleName().equals("Administrador")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            System.out.println("Rol asignado: ROLE_ADMIN");
        } else if (usuario.getClass().getSimpleName().equals("Cliente")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_CLIENT"));
            System.out.println("Rol asignado: ROLE_CLIENT");
        }

        // Guardar el usuario en la sesión para poder acceder a él en el controlador
        // Esto se hace en un listener de autenticación exitosa

        // Crear y devolver un objeto UserDetails con el correo, contraseña y roles
        // La contraseña ya está codificada en la base de datos, así que pasamos true para authorities.
        return new User(
                usuario.getCorreo(),
                usuario.getContrasena(), // Spring Security comparará esto con la contraseña ingresada
                true, // enabled
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                authorities
        );
    }
}