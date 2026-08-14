package com.hospitalsanjose.hospitalbackend.service;

import com.hospitalsanjose.hospitalbackend.dto.ReservaCitaDTO;
import com.hospitalsanjose.hospitalbackend.model.*;
import com.hospitalsanjose.hospitalbackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private ConsultorioRepository consultorioRepository;
    @Autowired
    private HorarioRepository horarioRepository;

    public Cita reservarCita(ReservaCitaDTO dto) {
        Paciente paciente = pacienteRepository.findById(dto.getIdPaciente())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + dto.getIdPaciente()));
        
        Medico medico = medicoRepository.findById(dto.getIdMedico())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado con ID: " + dto.getIdMedico()));
        
        Consultorio consultorio = consultorioRepository.findById(dto.getIdConsultorio() != null ? dto.getIdConsultorio() : 1)
                .orElseThrow(() -> new RuntimeException("Consultorio no encontrado"));
        
        Horario horario = horarioRepository.findById(dto.getIdHorario() != null ? dto.getIdHorario() : 1)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setMedico(medico);
        cita.setConsultorio(consultorio);
        cita.setHorario(horario);
        cita.setFecha(dto.getFechaCita());
        
        cita.setHoraCita(horario.getHoraInicio() != null ? horario.getHoraInicio() : LocalTime.of(8, 0));
        cita.setEstado("Pendiente");
        cita.setObservaciones(dto.getMotivoConsulta());

        return citaRepository.save(cita);
    }

    public Cita cancelarCita(Integer idCita) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        cita.setEstado("Cancelada");
        return citaRepository.save(cita);
    }

    // Método añadido para la lógica de borrado físico en repositorio
    public void eliminarCita(Integer idCita) {
        if (!citaRepository.existsById(idCita)) {
            throw new RuntimeException("Cita no encontrada con ID: " + idCita);
        }
        citaRepository.deleteById(idCita);
    }

    public List<Cita> obtenerCitasPorPaciente(Integer idPaciente) {
        return citaRepository.findByPacienteIdPaciente(idPaciente);
    }
}