package com.example.PizzUMBurgUM.controllers.DTOS;

import com.example.PizzUMBurgUM.entities.Domicilio;
import com.example.PizzUMBurgUM.entities.Tarjeta;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

@Setter
@Getter
public class RegistroClienteRequest {

    @NotBlank(message = "El nombre es obligatorio.")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio.")
    private String apellido;

    @NotNull(message = "La cédula es obligatoria.")
    private String cedula;

    @NotNull(message = "La fecha de nacimiento es obligatoria.")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El correo es obligatorio.")
    @Email(message = "Debe ingresar un correo válido.")
    private String correo;

    @NotBlank(message = "El teléfono es obligatorio.")
    @Pattern(regexp = "^09\\d{7}$", message = "El teléfono debe tener 9 dígitos y comenzar con 09.")
    private String telefono;

    @NotBlank(message = "La contraseña es obligatoria.")
    private String contrasena;

    @NotBlank(message = "Debe repetir la contraseña.")
    private String contrasena2;

    @NotNull(message = "Debe ingresar su dirección principal.")
    private DomicilioRequest domicilio;

    @NotNull(message = "Debe ingresar el número de su tarjeta.")
    private TarjetaRequest tarjeta;
}
