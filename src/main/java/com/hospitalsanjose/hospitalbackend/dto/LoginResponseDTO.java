package com.hospitalsanjose.hospitalbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String correo;
    private String rol;
    private Integer idUsuario;
}