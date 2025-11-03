package com.example.PizzUMBurgUM.controllers.DTOS;

import com.example.PizzUMBurgUM.entities.enums.TipoTarjeta;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter

public class TarjetaRequest {
    @NotNull
    private String numero;
    @NotNull
    private String nombreTitular;
    @NotNull
    private long clienteId;
    @NotNull
    private TipoTarjeta tipoTarjeta;
    @NotNull
    private Date fechaVencimiento;
    @NotNull
    private Boolean predeterminada;
}
