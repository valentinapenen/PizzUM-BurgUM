package com.example.PizzUMBurgUM.entities;

import com.example.PizzUMBurgUM.entities.enums.TipoCreacion;
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

    @OneToMany
    private List<Producto> productos;

    @NotNull
    private double precioTotal;

    @NotNull
    private boolean favorito;

    @NotNull
    @ManyToOne
    private Cliente cliente;
}
