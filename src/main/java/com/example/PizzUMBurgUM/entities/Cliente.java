package com.example.PizzUMBurgUM.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public class Cliente extends Usuario{

    @NotEmpty(message = "Debe de guardarse por lo menos un domicilio")
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "cliente_domicilio", joinColumns = @JoinColumn(name = "cliente_cedula"), inverseJoinColumns = @JoinColumn(name = "domicilio_id"))
    private List<Domicilio> domicilios = new ArrayList<>();

    @NotEmpty(message = "Debe de guardarse por lo menos una tarjeta")
    @OneToMany(mappedBy= "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tarjeta> tarjetas = new ArrayList<>();

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pedido> pedidos = new ArrayList<>();
    
}
