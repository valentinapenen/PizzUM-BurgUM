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
    private Long id;
    @NotNull
    private Double precioTotal;
    @NotNull
    @OneToOne
    @JoinColumn(name = "producto_base_id")
    private BaseProducto productoBase;
    @OneToMany
    private List<Topping> toppings;
    @NotNull
    private Boolean favorito;

    public Double calcularTotal() {
        return toppings.stream()
                .mapToDouble(Topping::getPrecioBase)
                .sum();
    }
}
