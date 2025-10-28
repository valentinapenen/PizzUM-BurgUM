package com.example.PizzUMBurgUM.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public class Producto {
    @Id
    @NotNull
    private long id;
    @NotNull
    private String nombre;
    @NotNull
    private double precioBase;
    @NotNull
    private boolean disponible; // para no borrarlo de la bd en caso de volver a ofrecerlo a futuro
}
