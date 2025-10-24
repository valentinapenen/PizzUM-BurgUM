package com.example.PizzUMBurgUM.entities;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public abstract class BaseProducto extends Producto{
    @NotNull
    private List<Topping> toppings;

    private double calcularPrecioFinal() {
        double total = this.getPrecioBase();
        for (Topping topping : this.toppings) {
            total += topping.getPrecioBase();
        }
        return total;
    }
}
