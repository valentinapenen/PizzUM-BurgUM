package com.example.PizzUMBurgUM.entities;

import com.example.PizzUMBurgUM.entities.enums.CategoriaTopping;
import com.example.PizzUMBurgUM.entities.enums.TipoTopping;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder

public class Hamburguesa extends BaseProducto{
    @NotNull
    private int cantidadCarnes;
    @NotNull
    @OneToOne
    @Enumerated(EnumType.STRING)
    private Topping tipoPan;
    @NotNull
    @OneToOne
    @Enumerated(EnumType.STRING)
    private Topping tipoCarne;

    public Hamburguesa(int cantidadCarnes, Topping tipoPan, Topping tipoCarne) {
        if (tipoPan.getTipo() != TipoTopping.PAN || tipoCarne.getTipo() != TipoTopping.PAN) {
            throw new IllegalArgumentException("Toppings esenciales inválidos");
        }

        this.cantidadCarnes = cantidadCarnes;
        this.tipoPan = tipoPan;
        this.tipoCarne = tipoCarne;
    }
}
