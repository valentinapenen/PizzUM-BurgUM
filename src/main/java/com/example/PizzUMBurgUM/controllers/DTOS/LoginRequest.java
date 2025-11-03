package com.example.PizzUMBurgUM.controllers.DTOS;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "El correo es obligatorio.")
    @Email
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria.")
    private String password;
}
