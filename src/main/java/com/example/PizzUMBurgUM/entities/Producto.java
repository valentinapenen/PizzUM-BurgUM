package com.example.PizzUMBurgUM.entities;

import com.example.PizzUMBurgUM.entities.enums.CategoriaProducto;
import com.example.PizzUMBurgUM.entities.enums.TipoProducto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_producto")
public class Producto {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String nombre;

    @NotNull
    private double precio;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TipoProducto tipo;  // MASA, QUESO, SALSA, PAN, CARNE, BEBIDA, ADEREZO,PAPAS BEBIDAS etc ETC.

    @Enumerated(EnumType.STRING)
    @NotNull
    private CategoriaProducto categoria; // PIZZA, HAMBURGUESA, AMBOS

    @NotNull
    private boolean disponible; // para no borrarlo de la bd en caso de volver a ofrecerlo a futuro
}
