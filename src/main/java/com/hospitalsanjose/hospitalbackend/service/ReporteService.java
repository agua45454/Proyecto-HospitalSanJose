package com.hospitalsanjose.hospitalbackend.service;

import com.hospitalsanjose.hospitalbackend.dto.ReporteResumenDTO;
import com.hospitalsanjose.hospitalbackend.model.Cita;
import com.hospitalsanjose.hospitalbackend.repository.CitaRepository;
import com.hospitalsanjose.hospitalbackend.repository.HistoriaClinicaRepository;
import com.hospitalsanjose.hospitalbackend.repository.MedicoRepository;
import com.hospitalsanjose.hospitalbackend.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private HistoriaClinicaRepository historiaClinicaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    public ReporteResumenDTO generarResumen() {
        List<Cita> citas = citaRepository.findAll();

        long total = citas.size();
        long pendientes = citas.stream().filter(c -> "Pendiente".equals(c.getEstado())).count();
        long confirmadas = citas.stream().filter(c -> "Confirmada".equals(c.getEstado())).count();
        long canceladas = citas.stream().filter(c -> "Cancelada".equals(c.getEstado())).count();
        long atendidas = citas.stream().filter(c -> "Atendida".equals(c.getEstado())).count();

        Map<String, Long> citasPorEspecialidad = citas.stream()
                .filter(c -> c.getMedico() != null && c.getMedico().getEspecialidad() != null)
                .collect(Collectors.groupingBy(
                        c -> c.getMedico().getEspecialidad().getNombre(),
                        LinkedHashMap::new,
                        Collectors.counting()
                ));

        return new ReporteResumenDTO(
                total,
                pendientes,
                confirmadas,
                canceladas,
                atendidas,
                historiaClinicaRepository.count(),
                pacienteRepository.count(),
                medicoRepository.count(),
                citasPorEspecialidad
        );
    }
}
