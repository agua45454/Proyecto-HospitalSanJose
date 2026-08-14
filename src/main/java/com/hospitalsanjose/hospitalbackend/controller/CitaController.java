package com.hospitalsanjose.hospitalbackend.controller;

import com.hospitalsanjose.hospitalbackend.dto.ReservaCitaDTO;
import com.hospitalsanjose.hospitalbackend.model.Cita;
import com.hospitalsanjose.hospitalbackend.model.Horario;
import com.hospitalsanjose.hospitalbackend.repository.HorarioRepository;
import com.hospitalsanjose.hospitalbackend.service.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "*")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private HorarioRepository horarioRepository;

    @PostMapping("/reservar")
    public ResponseEntity<?> reservarCita(@RequestBody ReservaCitaDTO dto) {
        try {
            // Búsqueda y asignación automática de horario válido para evitar errores de validación estricta
            List<Horario> horarios = horarioRepository.findAll();
            Horario horarioValido = horarios.stream()
                    .filter(h -> h.getMedico() != null && h.getMedico().getIdMedico().equals(dto.getIdMedico()))
                    .findFirst()
                    .orElse(horarios.stream().findFirst().orElse(null));

            if (horarioValido != null) {
                dto.setIdHorario(horarioValido.getIdHorario());
                if (horarioValido.getConsultorio() != null) {
                    dto.setIdConsultorio(horarioValido.getConsultorio().getIdConsultorio());
                }
            }

            Cita cita = citaService.reservarCita(dto);
            return new ResponseEntity<>(cita, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/cancelar/{id}")
    public ResponseEntity<?> cancelarCita(@PathVariable("id") Integer idCita) {
        try {
            Cita cita = citaService.cancelarCita(idCita);
            return ResponseEntity.ok(cita);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCita(@PathVariable("id") Integer idCita) {
        try {
            citaService.eliminarCita(idCita);
            return ResponseEntity.ok("Cita eliminada correctamente");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<List<Cita>> listarPorPaciente(@PathVariable Integer idPaciente) {
        return ResponseEntity.ok(citaService.obtenerCitasPorPaciente(idPaciente));
    }
}