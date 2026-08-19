package com.hospitalsanjose.hospitalbackend.dto;

import lombok.Data;

@Data
public class RegistroMedicoDTO {
    private String nombres;
    private String apellidos;
    private String colegiatura;
    private String especialidad;
    private String telefono;
    private String correo;
    private String password;
}