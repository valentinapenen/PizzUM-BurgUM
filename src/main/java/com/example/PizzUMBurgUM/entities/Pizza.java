package com.example.PizzUMBurgUM.entities;

import com.example.PizzUMBurgUM.entities.enums.CategoriaTopping;
import com.example.PizzUMBurgUM.entities.enums.TamanoPizza;
import com.example.PizzUMBurgUM.entities.enums.TipoTopping;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Pizza extends BaseProducto {
    @NotNull
    @Enumerated(EnumType.STRING)
    private TamanoPizza tamano;
    @OneToOne
    @JoinColumn(name = "masa_id")
    @NotNull
    private Topping masa;
    @OneToOne
    @JoinColumn(name = "queso_id")
    @NotNull
    private Topping queso;
    @OneToOne
    @JoinColumn(name = "salsa_id")
    @NotNull
    private Topping salsa;

    public Pizza(Topping masa, Topping queso, Topping salsa) {
        if (masa.getTipo() != TipoTopping.MASA || queso.getTipo() != TipoTopping.QUESO || salsa.getTipo() != TipoTopping.SALSA) {
            throw new IllegalArgumentException("Toppings esenciales inválidos");
        }
        this.masa = masa;
        this.queso = queso;
        this.salsa = salsa;
    }
}
