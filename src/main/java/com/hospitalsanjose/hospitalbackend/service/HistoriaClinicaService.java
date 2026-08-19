package com.hospitalsanjose.hospitalbackend.service;

import com.hospitalsanjose.hospitalbackend.dto.HistoriaClinicaDTO;
import com.hospitalsanjose.hospitalbackend.model.Cita;
import com.hospitalsanjose.hospitalbackend.model.HistoriaClinica;
import com.hospitalsanjose.hospitalbackend.model.Medico;
import com.hospitalsanjose.hospitalbackend.model.Paciente;
import com.hospitalsanjose.hospitalbackend.repository.CitaRepository;
import com.hospitalsanjose.hospitalbackend.repository.HistoriaClinicaRepository;
import com.hospitalsanjose.hospitalbackend.repository.MedicoRepository;
import com.hospitalsanjose.hospitalbackend.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HistoriaClinicaService {

    @Autowired
    private HistoriaClinicaRepository historiaClinicaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private CitaRepository citaRepository;

    @Transactional
    public HistoriaClinica registrar(HistoriaClinicaDTO dto) {
        if (dto.getIdPaciente() == null) {
            throw new RuntimeException("Debe indicar el paciente.");
        }
        if (dto.getIdMedico() == null) {
            throw new RuntimeException("Debe indicar el médico.");
        }
        if (dto.getDiagnostico() == null || dto.getDiagnostico().isBlank()) {
            throw new RuntimeException("El diagnóstico es obligatorio.");
        }

        Paciente paciente = pacienteRepository.findById(dto.getIdPaciente())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + dto.getIdPaciente()));

        Medico medico = medicoRepository.findById(dto.getIdMedico())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado con ID: " + dto.getIdMedico()));

        HistoriaClinica historia = new HistoriaClinica();
        historia.setPaciente(paciente);
        historia.setMedico(medico);
        historia.setMotivoConsulta(dto.getMotivoConsulta());
        historia.setDiagnostico(dto.getDiagnostico());
        historia.setTratamiento(dto.getTratamiento());
        historia.setObservaciones(dto.getObservaciones());

        // Si la atención proviene de una cita ya reservada (Sprint 2),
        // se enlaza y se marca la cita como "Atendida" (no rompe el flujo
        // existente de Pendiente/Confirmada/Cancelada, solo añade el estado
        // final del ciclo de vida de la cita).
        if (dto.getIdCita() != null) {
            Cita cita = citaRepository.findById(dto.getIdCita()).orElse(null);
            if (cita != null) {
                historia.setCita(cita);
                cita.setEstado("Atendida");
                citaRepository.save(cita);
            }
        }

        return historiaClinicaRepository.save(historia);
    }

    public List<HistoriaClinica> listarPorPaciente(Integer idPaciente) {
        return historiaClinicaRepository.findByPacienteIdPacienteOrderByFechaAtencionDesc(idPaciente);
    }

    public List<HistoriaClinica> listarPorMedico(Integer idMedico) {
        return historiaClinicaRepository.findByMedicoIdMedicoOrderByFechaAtencionDesc(idMedico);
    }
}
