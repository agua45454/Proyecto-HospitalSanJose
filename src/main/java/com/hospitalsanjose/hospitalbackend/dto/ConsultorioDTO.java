package com.hospitalsanjose.hospitalbackend.dto;

import lombok.Data;

@Data
public class ConsultorioDTO {
    private String numero;
    private String piso;
    private String ubicacion;
    private String estado; // Disponible | Ocupado | Mantenimiento
}
