package com.example.PizzUMBurgUM.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Creacion {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private double precioTotal;
    @NotNull
    @OneToOne
    @JoinColumn(name = "producto_base_id")
    private BaseProducto productoBase;
    @OneToMany
    private List<Topping> toppings;
    @NotNull
    private boolean favorito;
    @NotNull
    @ManyToOne
    private Pedido pedido;

    public void calcularTotal() { //calculo el precioTotal directamente en la clase ya que utiliza datos que guarda, por lo que no es necesario buscarlos en la bd
        double precioToppings = toppings.stream()
                .mapToDouble(Topping::getPrecioBase)
                .sum();
        precioTotal = precioToppings + productoBase.getPrecioBase();
    }
}
