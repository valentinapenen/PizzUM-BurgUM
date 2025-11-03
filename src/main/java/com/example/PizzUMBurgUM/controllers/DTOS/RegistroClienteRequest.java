package com.example.PizzUMBurgUM.controllers.DTOS;

import com.example.PizzUMBurgUM.entities.Domicilio;
import com.example.PizzUMBurgUM.entities.Tarjeta;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class RegistroClienteRequest {

    @NotBlank(message = "El nombre es obligatorio.")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio.")
    private String apellido;

    @NotNull(message = "La cédula es obligatoria.")
    private Long cedula;

    @NotNull(message = "La fecha de nacimiento es obligatoria.")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El correo es obligatorio.")
    @Email(message = "Debe ingresar un correo válido.")
    private String correo;

    @NotBlank(message = "El teléfono es obligatorio.")
    private String telefono;

    @NotBlank(message = "La contraseña es obligatoria.")
    private String contrasena;

    // Estos pueden ser campos simples que después transformás en objetos
    @NotNull(message = "Debe ingresar su dirección principal.")
    private DomicilioRequest domicilio;

    @NotNull(message = "Debe ingresar el número de su tarjeta.")
    private TarjetaRequest Tarjeta;
}
