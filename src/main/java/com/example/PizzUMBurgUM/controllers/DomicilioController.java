package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.controllers.DTOS.DomicilioRequest;
import com.example.PizzUMBurgUM.entities.Domicilio;
import com.example.PizzUMBurgUM.repositories.DomicilioRepository;
import com.example.PizzUMBurgUM.services.DomicilioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DomicilioController {
    @Autowired
    private DomicilioService domicilioService;

    //@PostMapping
    //public ResponseEntity<Domicilio> crearDomicilio(@RequestBody DomicilioRequest request) {
    //    Domicilio nuevoDomicilio = domicilioService.crearDomicilio(
    //            request.getNumero(),
    //            request.getCalle(),
    //            request.getDepartamento(),
    //            request.getCiudad(),
    //            request.getApartamento(),
    //            request.getPredeterminado(),
    //    );
    //    return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDomicilio);
    //}

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDomicilio(@RequestParam long idDomicilio) {
        domicilioService.eliminarDomicilio(idDomicilio);
        return ResponseEntity.noContent().build();
    }

    @PutMapping()
    public ResponseEntity<Domicilio> marcarPredeterminado(@RequestParam long clienteId, @RequestParam long domicilioId) {
        return ResponseEntity.ok(domicilioService.marcarPredeterminado(clienteId, domicilioId));
    }
}
