package com.example.PizzUMBurgUM.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Tarjeta {
    @Id
    @NotNull
    private String numero;
    @NotNull
    private String nombre_titular;
    @NotNull
    private String apellido_titular;
    @NotNull
    private Date fecha_vencimiento;

}
