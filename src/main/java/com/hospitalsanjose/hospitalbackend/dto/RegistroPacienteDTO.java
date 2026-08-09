package com.hospitalsanjose.hospitalbackend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RegistroPacienteDTO {
    private String nombres;
    private String apellidos;
    private String dni;
    private LocalDate fechaNacimiento;
    private String correo;
    private String telefono;
    private String direccion;
    private String genero;
    private String password;
}