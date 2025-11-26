package com.example.PizzUMBurgUM.entities;

import com.example.PizzUMBurgUM.entities.enums.EstadoPedido;
import com.example.PizzUMBurgUM.entities.enums.MedioDePago;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private long idPedido;

    @NotNull
    private EstadoPedido estado;

    @NotNull
    private LocalDateTime fecha;

    @NotNull
    private double total;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MedioDePago medioDePago;

    @NotNull
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "pedido_creaciones", joinColumns = @JoinColumn(name = "pedido_id"), inverseJoinColumns = @JoinColumn(name = "creacion_id"))
    private List<Creacion> creaciones;

    @ManyToOne
    @NotNull
    private Domicilio domicilio;

    @ManyToOne
    @NotNull
    private Cliente cliente;
}
