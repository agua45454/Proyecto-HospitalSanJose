package com.hospitalsanjose.hospitalbackend.controller;

import com.hospitalsanjose.hospitalbackend.dto.LoginRequestDTO;
import com.hospitalsanjose.hospitalbackend.dto.LoginResponseDTO;
import com.hospitalsanjose.hospitalbackend.dto.RegistroMedicoDTO;
import com.hospitalsanjose.hospitalbackend.dto.RegistroPacienteDTO;
import com.hospitalsanjose.hospitalbackend.service.AuthService;
import com.hospitalsanjose.hospitalbackend.service.MedicoService;
import com.hospitalsanjose.hospitalbackend.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private MedicoService medicoService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO dto) {
        try {
            return ResponseEntity.ok(authService.login(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/registro/paciente")
    public ResponseEntity<?> registrarPaciente(@RequestBody RegistroPacienteDTO dto) {
        try {
            pacienteService.registrarPaciente(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Paciente registrado correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/registro/medico")
    public ResponseEntity<?> registrarMedico(@RequestBody RegistroMedicoDTO dto) {
        try {
            medicoService.registrarMedico(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Médico registrado correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}