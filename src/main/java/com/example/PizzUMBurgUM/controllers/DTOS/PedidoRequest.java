package com.example.PizzUMBurgUM.controllers.DTOS;

import com.example.PizzUMBurgUM.entities.Cliente;
import com.example.PizzUMBurgUM.entities.Domicilio;
import com.example.PizzUMBurgUM.entities.enums.MedioDePago;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.List;

@Getter
@Setter

public class PedidoRequest {

    private Cliente cliente;
    private List<Long> idsCreaciones;
    @ManyToOne
    private Domicilio domicilio;
    private MedioDePago medioDePago;
}
