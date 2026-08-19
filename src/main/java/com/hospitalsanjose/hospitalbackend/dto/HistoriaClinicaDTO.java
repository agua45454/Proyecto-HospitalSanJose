package com.hospitalsanjose.hospitalbackend.dto;

import lombok.Data;

@Data
public class HistoriaClinicaDTO {
    private Integer idPaciente;
    private Integer idMedico;
    private Integer idCita; // opcional: cita de origen de la atención
    private String motivoConsulta;
    private String diagnostico;
    private String tratamiento;
    private String observaciones;
}
