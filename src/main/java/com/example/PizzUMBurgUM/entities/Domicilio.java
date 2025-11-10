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
    @NotNull
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

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToOne
    @JoinColumn(name = "administrador_id")
    private Administrador administrador;

    public Domicilio(String numero, String calle, String departamento, String ciudad, String apartamento, boolean predeterminado, Cliente cliente) {
        this.numero = numero;
        this.calle = calle;
        this.departamento = departamento;
        this.ciudad = ciudad;
        this.apartamento = apartamento;
        this.predeterminado = predeterminado;
        this.cliente = cliente;
    }

    public Domicilio(String numero, String calle, String departamento, String ciudad, String apartamento, boolean predeterminado, Administrador administrador) {
        this.numero = numero;
        this.calle = calle;
        this.departamento = departamento;
        this.ciudad = ciudad;
        this.apartamento = apartamento;
    }
}
