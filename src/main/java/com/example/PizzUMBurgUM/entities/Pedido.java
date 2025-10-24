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
    @OneToMany
    private List<Creacion> creaciones;
    @NotNull
    @ManyToOne
    private Cliente cliente;
    @ManyToOne
    @NotNull
    private Domicilio domicilio;

    public double calcularTotal() {
        return creaciones.stream()
                .mapToDouble(Creacion::getPrecioTotal)
                .sum();
    }
}
