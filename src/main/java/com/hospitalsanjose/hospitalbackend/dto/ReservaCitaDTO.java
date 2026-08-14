package com.hospitalsanjose.hospitalbackend.dto;

import java.time.LocalDate;

public class ReservaCitaDTO {
    private Integer idPaciente;
    private Integer idMedico;
    private Integer idEspecialidad;
    private Integer idConsultorio;
    private Integer idHorario;
    private LocalDate fechaCita;
    private String motivoConsulta;

    // Getters y Setters
    public Integer getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Integer idPaciente) { this.idPaciente = idPaciente; }

    public Integer getIdMedico() { return idMedico; }
    public void setIdMedico(Integer idMedico) { this.idMedico = idMedico; }

    public Integer getIdEspecialidad() { return idEspecialidad; }
    public void setIdEspecialidad(Integer idEspecialidad) { this.idEspecialidad = idEspecialidad; }

    public Integer getIdConsultorio() { return idConsultorio; }
    public void setIdConsultorio(Integer idConsultorio) { this.idConsultorio = idConsultorio; }

    public Integer getIdHorario() { return idHorario; }
    public void setIdHorario(Integer idHorario) { this.idHorario = idHorario; }

    public LocalDate getFechaCita() { return fechaCita; }
    public void setFechaCita(LocalDate fechaCita) { this.fechaCita = fechaCita; }

    public String getMotivoConsulta() { return motivoConsulta; }
    public void setMotivoConsulta(String motivoConsulta) { this.motivoConsulta = motivoConsulta; }
}