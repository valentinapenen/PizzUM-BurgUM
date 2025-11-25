package com.example.PizzUMBurgUM.entities;

import com.example.PizzUMBurgUM.entities.enums.TamanoPizza;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrecioTamanoPizza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private TamanoPizza tamano;

    @NotNull
    private double precioBase;
}
