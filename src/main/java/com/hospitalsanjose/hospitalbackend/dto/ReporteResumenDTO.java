package com.hospitalsanjose.hospitalbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ReporteResumenDTO {
    private long totalCitas;
    private long citasPendientes;
    private long citasConfirmadas;
    private long citasCanceladas;
    private long citasAtendidas;
    private long totalHistoriasClinicas;
    private long totalPacientes;
    private long totalMedicos;
    private Map<String, Long> citasPorEspecialidad;
}
