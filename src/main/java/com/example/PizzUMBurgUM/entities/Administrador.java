package com.example.PizzUMBurgUM.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Administrador extends Usuario{

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @NotNull
    private Domicilio domicilio_facturacion;


}
