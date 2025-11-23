package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.controllers.DTOS.TarjetaRequest;
import com.example.PizzUMBurgUM.entities.Tarjeta;
import com.example.PizzUMBurgUM.services.TarjetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tarjetas")
public class TarjetaRestController {

    @Autowired
    private TarjetaService tarjetaServicio;


    @GetMapping("/{numero}")
    public ResponseEntity<TarjetaRequest> obtenerTarjetaPorNumero(@PathVariable String numero) {

        Tarjeta tarjeta = tarjetaServicio.buscarTarjetaPorNumero(numero);

        if (tarjeta == null) {
            return ResponseEntity.notFound().build();
        }


        TarjetaRequest dto = new TarjetaRequest();
        dto.setNumero(tarjeta.getNumero());
        dto.setNombreTitular(tarjeta.getNombreTitular());
        dto.setClienteId(tarjeta.getCliente().getId());
        dto.setTipoTarjeta(tarjeta.getTipoTarjeta());
        dto.setFechaVencimiento(tarjeta.getFecha_vencimiento());
        dto.setPredeterminada(tarjeta.isPredeterminada());

        return ResponseEntity.ok(dto);
    }
}
