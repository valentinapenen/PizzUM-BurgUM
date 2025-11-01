package com.example.PizzUMBurgUM.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Domicilio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String numero;
    @NotNull
    private String calle;
    @NotNull
    private String departamento;
    @NotNull
    private String ciudad;
    private String apartamento;
    @NotNull
    private boolean predeterminado;
}
