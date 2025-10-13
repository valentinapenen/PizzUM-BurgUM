package com.example.PizzUMBurgUM.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Domicilio {
    @Id
    @NotNull
    private String nro_puerta;
    @Id
    @NotNull
    private String nro_apto;
    @NotNull
    private String calle;
}
