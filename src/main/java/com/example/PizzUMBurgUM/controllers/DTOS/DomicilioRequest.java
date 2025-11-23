package com.example.PizzUMBurgUM.controllers.DTOS;

import com.example.PizzUMBurgUM.entities.Administrador;
import com.example.PizzUMBurgUM.entities.Cliente;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class DomicilioRequest {
    @NotNull
    private String numero;
    @NotNull
    private String calle;
    @NotNull
    private String departamento;
    @NotNull
    private String ciudad;
    private String apartamento;
    @NotNull
    private Boolean predeterminado;
}
