package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.controllers.DTOS.PedidoDGIDTO;
import com.example.PizzUMBurgUM.entities.Pedido;
import com.example.PizzUMBurgUM.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/dgi")
public class DgiRestController {

    @Autowired
    private PedidoService pedidoServicio;


    @GetMapping("/tickets")
    public ResponseEntity<List<PedidoDGIDTO>> obtenerPedidosPorFecha(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        LocalDateTime desde = fecha.atStartOfDay();
        LocalDateTime hasta = fecha.atTime(LocalTime.MAX);


        List<Pedido> pedidos = pedidoServicio.listarPedidosPorFecha(desde, hasta);

        // Convertir pedidos a DTO
        List<PedidoDGIDTO> resultado = new ArrayList<>();

        for (Pedido p : pedidos) {
            PedidoDGIDTO dto = new PedidoDGIDTO();
            dto.setId(p.getIdPedido());
            dto.setFecha(p.getFecha());
            dto.setTotal(p.getTotal());
            dto.setMedioDePago(p.getMedioDePago().name());
            dto.setIdCliente(p.getCliente().getId());

            resultado.add(dto);
        }

        return ResponseEntity.ok(resultado);
    }
}


// GET /api/dgi/tickets?fecha=YYYY-MM-DD
//http://localhost:8080/api/dgi/tickets?fecha=2025-11-22