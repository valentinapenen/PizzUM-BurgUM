package com.example.PizzUMBurgUM.entities;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;
import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public abstract class Usuario {

    @NotNull
    @Column(nullable = false)
    private String nombre;

    @NotNull
    @Column(nullable = false)
    private String apellido;

    @Id
    @Column(length = 8, nullable = false, unique = true)
    @NotNull
    private int cedula;

    @NotNull
    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    @NotNull
    @Email
    @Column(unique = true)
    private String correo;


    @NotNull
    @Column(nullable = false)
    private String telefono;

    @NotNull
    @Column(nullable = false)
    private String contrasena;

}
