package com.example.PizzUMBurgUM.repositories;


import com.example.PizzUMBurgUM.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    Usuario findByCorreo(String correo);
    boolean existsByCorreo(String correo);
}
