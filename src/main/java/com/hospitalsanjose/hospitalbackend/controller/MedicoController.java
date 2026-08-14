package com.hospitalsanjose.hospitalbackend.controller;

import com.hospitalsanjose.hospitalbackend.model.Medico;
import com.hospitalsanjose.hospitalbackend.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medicos")
@CrossOrigin(origins = "*")
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;

    @GetMapping
    public ResponseEntity<?> listarMedicos() {
        try {
            List<Medico> medicos = medicoRepository.findAll();
            
            List<Map<String, Object>> respuesta = medicos.stream().map(medico -> {
                Map<String, Object> map = new HashMap<>();
                map.put("idMedico", medico.getIdMedico());
                map.put("nombres", medico.getNombres());
                map.put("apellidos", medico.getApellidos());
                
                if (medico.getEspecialidad() != null) {
                    Map<String, Object> espMap = new HashMap<>();
                    espMap.put("nombre", medico.getEspecialidad().getNombre());
                    map.put("especialidad", espMap);
                } else {
                    map.put("especialidad", null);
                }
                return map;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}