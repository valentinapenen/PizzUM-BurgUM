package com.example.PizzUMBurgUM.entities;

import com.example.PizzUMBurgUM.entities.enums.TipoCreacion;
import com.example.PizzUMBurgUM.entities.enums.TamanoPizza;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Creacion {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TipoCreacion tipo; // PIZZA o HAMBURGUESA

    // Cambié a ManyToMany con una tabla intermedia NUEVA para evitar la
    // restricción única que se genera por el mapeo OneToMany que teniamos antes.
    // La tabla anterior (creacion_productos) tenía una unique constraint sobre
    // productos_id que no dejaba reutilizar el mismo producto en distintas creaciones.
    @ManyToMany
    @JoinTable(
            name = "creacion_productos_items",
            joinColumns = @JoinColumn(name = "creacion_id"),
            inverseJoinColumns = @JoinColumn(name = "producto_id")
    )
    private List<Producto> productos;

    @Enumerated(EnumType.STRING)
    private TamanoPizza tamanoPizza; // solo aplica cuando tipo = PIZZA

    @NotNull
    private double precioTotal;

    @NotNull
    private boolean favorito;

    // Indica si la creación está actualmente en el carrito del cliente.
    // Si se marca como favorita y el usuario la elimina del carrito, NO se borra la creación,
    // sólo se marca enCarrito = false para que desaparezca del carrito pero siga en Favoritos.
    @NotNull
    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean enCarrito;

    @NotNull
    @ManyToOne
    private Cliente cliente;
}
