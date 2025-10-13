package com.example.PizzUMBurgUM.entities;

import jakarta.persistence.Entity;
import lombok.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Cliente extends Usuario{
    private List<Domicilio> domicilios;
    private List<Tarjeta> tarjetas;
    private List<Pedido> pedidos;

}
