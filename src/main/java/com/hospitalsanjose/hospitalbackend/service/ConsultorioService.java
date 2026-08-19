package com.hospitalsanjose.hospitalbackend.service;

import com.hospitalsanjose.hospitalbackend.dto.ConsultorioDTO;
import com.hospitalsanjose.hospitalbackend.model.Consultorio;
import com.hospitalsanjose.hospitalbackend.repository.ConsultorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConsultorioService {

    @Autowired
    private ConsultorioRepository consultorioRepository;

    public List<Consultorio> listarTodos() {
        return consultorioRepository.findAll();
    }

    @Transactional
    public Consultorio crear(ConsultorioDTO dto) {
        if (dto.getNumero() == null || dto.getNumero().isBlank()) {
            throw new RuntimeException("El número de consultorio es obligatorio.");
        }

        Consultorio consultorio = new Consultorio();
        consultorio.setNumero(dto.getNumero());
        consultorio.setPiso(dto.getPiso());
        consultorio.setUbicacion(dto.getUbicacion());
        consultorio.setEstado(dto.getEstado() != null && !dto.getEstado().isBlank() ? dto.getEstado() : "Disponible");
        return consultorioRepository.save(consultorio);
    }

    @Transactional
    public Consultorio actualizar(Integer id, ConsultorioDTO dto) {
        Consultorio consultorio = consultorioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultorio no encontrado con ID: " + id));

        consultorio.setNumero(dto.getNumero());
        consultorio.setPiso(dto.getPiso());
        consultorio.setUbicacion(dto.getUbicacion());
        if (dto.getEstado() != null && !dto.getEstado().isBlank()) {
            consultorio.setEstado(dto.getEstado());
        }
        return consultorioRepository.save(consultorio);
    }
}
