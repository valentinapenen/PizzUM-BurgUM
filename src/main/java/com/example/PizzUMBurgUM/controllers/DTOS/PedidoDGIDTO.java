package com.example.PizzUMBurgUM.controllers.DTOS;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PedidoDGIDTO {
    private long id;
    private LocalDateTime fecha;
    private double total;
    private String medioDePago;
    private long idCliente;
}
