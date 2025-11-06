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

    @NotNull(message = "Debe de guardarse por lo menos un domicilio")
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Domicilio> domicilios = new ArrayList<>();

    @NotNull(message = "Debe de guardarse por lo menos una tarjeta")
    @OneToMany(mappedBy= "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tarjeta> tarjetas = new ArrayList<>();

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pedido> pedidos = new ArrayList<>();
    
}
