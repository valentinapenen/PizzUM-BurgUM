package com.example.PizzUMBurgUM.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
    private String id;
    @NotNull
    private int precio_base;
    @NotNull
    private int precio_total;
    @NotNull
    private Boolean favorito;
    @NotNull
    private List<Producto> productos_adicionales;
}
