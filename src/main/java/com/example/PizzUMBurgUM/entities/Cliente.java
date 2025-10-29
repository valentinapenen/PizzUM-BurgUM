package com.example.PizzUMBurgUM.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public class Cliente extends Usuario{

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @NotNull
    private List<Domicilio> domicilios;

    @NotNull
    private List<Tarjeta> tarjetas;

    @NotNull
    private List<Pedido> pedidos;


    //public boolean agregarDomicilio (Domicilio domicilio_nuevo){
    //    domicilio_nuevo.setCliente(this):
    //    return getDomicilios().add(domicilio_nuevo);
    //}

}
