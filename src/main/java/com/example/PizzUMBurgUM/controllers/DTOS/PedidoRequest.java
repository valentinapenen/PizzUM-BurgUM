package com.example.PizzUMBurgUM.controllers.DTOS;

import com.example.PizzUMBurgUM.entities.Cliente;
import com.example.PizzUMBurgUM.entities.Domicilio;
import com.example.PizzUMBurgUM.entities.enums.MedioDePago;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter

public class PedidoRequest {
    @NotNull
    private Cliente cliente;
    @NotNull
    private List<Long> idsCreaciones;
    @NotNull
    @ManyToOne
    private Domicilio domicilio;
    @NotNull
    private MedioDePago medioDePago;
}
