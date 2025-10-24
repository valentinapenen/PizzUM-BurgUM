package com.example.PizzUMBurgUM.entities;
import com.example.PizzUMBurgUM.entities.enums.CategoriaTopping;
import com.example.PizzUMBurgUM.entities.enums.TipoTopping;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Topping extends Producto{
    @NotNull
    @Enumerated(EnumType.STRING)
    public static TipoTopping tipo; // salsa, masa, queso, pan, tipo de carne, aderezo, ingrediente extra, bebida
    @NotNull
    @Enumerated(EnumType.STRING)
    private CategoriaTopping categoria; // pizza, hamburguesa, ambos


    public static TipoTopping getTipo() {
        return tipo;
    }

    public CategoriaTopping getCategoria() {
        return categoria;
    }

    public Topping(TipoTopping tipo, CategoriaTopping categoria) {
        if ((TipoTopping.MASA.equals(tipo) && !CategoriaTopping.PIZZA.equals(categoria)) || (TipoTopping.SALSA.equals(tipo) && !CategoriaTopping.PIZZA.equals(categoria)) || (TipoTopping.QUESO.equals(tipo) && !CategoriaTopping.PIZZA.equals(categoria)) || (TipoTopping.PAN.equals(tipo) && !CategoriaTopping.HAMBURGUESA.equals(categoria)) || (TipoTopping.CARNE.equals(tipo) && !CategoriaTopping.HAMBURGUESA.equals(categoria))) {
            throw new IllegalArgumentException("El tipo de topping no corresponde a esa categoria");
        }

        this.tipo = tipo;
        this.categoria = categoria;
    }
}
