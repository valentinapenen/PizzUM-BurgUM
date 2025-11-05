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
    @NotNull
    @ManyToOne
    private Cliente cliente;

    public Domicilio(String numero, String calle, String departamento, String ciudad, String apartamento, boolean predeterminado) {
        this.numero = numero;
        this.calle = calle;
        this.departamento = departamento;
        this.ciudad = ciudad;
        this.apartamento = apartamento;
        this.predeterminado = predeterminado;
    }
}
