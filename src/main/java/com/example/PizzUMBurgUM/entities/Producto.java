package com.example.PizzUMBurgUM.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Producto {
    @Id
    @NotNull
    private String id;
    @NotNull
    private String nombre;
    @NotNull
    private int precio;

}
