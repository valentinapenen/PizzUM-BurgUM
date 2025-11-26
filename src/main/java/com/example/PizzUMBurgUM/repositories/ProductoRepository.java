package com.example.PizzUMBurgUM.repositories;

import com.example.PizzUMBurgUM.entities.Producto;
import com.example.PizzUMBurgUM.entities.enums.CategoriaProducto;
import com.example.PizzUMBurgUM.entities.enums.TipoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByDisponibleTrue();

    List<Producto> findByCategoria(CategoriaProducto categoria);

    List<Producto> findByTipo(TipoProducto tipo);

    //necesario para pizza y hamburguesa
    List<Producto> findByTipoAndCategoriaInAndDisponibleTrue(
            TipoProducto tipo,
            List<CategoriaProducto> categorias
    );
}
