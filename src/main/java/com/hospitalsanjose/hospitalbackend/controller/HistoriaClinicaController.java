package com.hospitalsanjose.hospitalbackend.controller;

import com.hospitalsanjose.hospitalbackend.dto.HistoriaClinicaDTO;
import com.hospitalsanjose.hospitalbackend.model.HistoriaClinica;
import com.hospitalsanjose.hospitalbackend.service.HistoriaClinicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historia-clinica")
@CrossOrigin(origins = "*")
public class HistoriaClinicaController {

    @Autowired
    private HistoriaClinicaService historiaClinicaService;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody HistoriaClinicaDTO dto) {
        try {
            HistoriaClinica historia = historiaClinicaService.registrar(dto);
            return new ResponseEntity<>(historia, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<List<HistoriaClinica>> listarPorPaciente(@PathVariable Integer idPaciente) {
        return ResponseEntity.ok(historiaClinicaService.listarPorPaciente(idPaciente));
    }

    @GetMapping("/medico/{idMedico}")
    public ResponseEntity<List<HistoriaClinica>> listarPorMedico(@PathVariable Integer idMedico) {
        return ResponseEntity.ok(historiaClinicaService.listarPorMedico(idMedico));
    }
}
