package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.controllers.DTOS.TarjetaRequest;
import com.example.PizzUMBurgUM.entities.Tarjeta;
import com.example.PizzUMBurgUM.services.TarjetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TarjetaController {
    @Autowired
    private TarjetaService tarjetaService;

//    @PostMapping
//    public ResponseEntity<Tarjeta> crearTarjeta(@RequestBody TarjetaRequest request) {
//        Tarjeta tarjeta = tarjetaService.crearTarjeta(
//                request.getNumero(),
//                request.getNombreTitular(),
//                request.getClienteId(),
//                request.getTipoTarjeta(),
//                request.getFechaVencimiento(),
//                request.getPredeterminada()
//        );
//
//        return ResponseEntity.ok(tarjetaService.crearTarjeta(tarjeta));
//    }

    @DeleteMapping
    public ResponseEntity<Void> eliminarTarjeta(@RequestParam long idTarjeta) {
        tarjetaService.eliminarTarjeta(idTarjeta);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Tarjeta> marcarPredeterminada(@RequestParam long clienteId, @RequestParam long tarjetaId) {
        return ResponseEntity.ok(tarjetaService.marcarPredeterminada(clienteId, tarjetaId));
    }
}
