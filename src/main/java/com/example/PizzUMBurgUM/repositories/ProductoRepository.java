package com.example.PizzUMBurgUM.repositories;

import com.example.PizzUMBurgUM.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByActivoTrue();
    List<Producto> findByNombreContaining(String nombre);
}
