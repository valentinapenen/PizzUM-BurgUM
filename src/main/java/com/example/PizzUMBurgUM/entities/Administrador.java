package com.example.PizzUMBurgUM.entities;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Administrador extends Usuario{
    @NotNull
    private String domicilio_facturacion;
}
