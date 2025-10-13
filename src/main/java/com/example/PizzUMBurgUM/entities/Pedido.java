package com.example.PizzUMBurgUM.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Pedido {
    @Id
    @NotNull
    private String id_pedido;
    @NotNull
    private String estado;
    @NotNull
    private Date fecha;
    @NotNull
    private List<Creacion> creaciones;
}
