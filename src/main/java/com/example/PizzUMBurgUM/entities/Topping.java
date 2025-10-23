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
    public static TipoTopping tipo; // aderezo, ingrediente extra, bebida
    @NotNull
    @Enumerated(EnumType.STRING)
    private CategoriaTopping categoria; // pizza, hamburguesa o ambos


    public static TipoTopping getTipo() {
        return tipo;
    }

    public CategoriaTopping getCategoria() {
        return categoria;
    }
}
