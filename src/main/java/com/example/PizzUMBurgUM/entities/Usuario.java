package com.example.PizzUMBurgUM.entities;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public abstract class Usuario {
    @NotNull
    private String nombre;

    @NotNull
    private String apellido;

    @Id
    @Column(length = 8)
    @NotNull
    private int cedula;

    @NotNull
    private Date fechaNacimiento;

    @NotNull
    private String correo;


    @NotNull
    private String telefono;

    @NotNull
    private String contrasena;





}
