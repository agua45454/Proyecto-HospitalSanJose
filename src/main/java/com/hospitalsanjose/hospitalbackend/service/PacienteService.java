package com.hospitalsanjose.hospitalbackend.service;

import com.hospitalsanjose.hospitalbackend.dto.RegistroPacienteDTO;
import com.hospitalsanjose.hospitalbackend.model.Paciente;
import com.hospitalsanjose.hospitalbackend.model.Rol;
import com.hospitalsanjose.hospitalbackend.model.Usuario;
import com.hospitalsanjose.hospitalbackend.repository.PacienteRepository;
import com.hospitalsanjose.hospitalbackend.repository.RolRepository;
import com.hospitalsanjose.hospitalbackend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PacienteService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void registrarPaciente(RegistroPacienteDTO dto) {
        // Validar correo único
        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new RuntimeException("El correo ya se encuentra registrado.");
        }

        // Validar DNI único
        if (pacienteRepository.existsByDni(dto.getDni())) {
            throw new RuntimeException("El DNI ya se encuentra registrado.");
        }

        // 1. Obtener Rol
        Rol rolPaciente = rolRepository.findByNombreRol("Paciente")
                .orElseThrow(() -> new RuntimeException("Error: El rol 'Paciente' no existe en la base de datos."));

        // 2. Crear Usuario
        Usuario usuario = new Usuario();
        usuario.setCorreo(dto.getCorreo());
        usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol(rolPaciente);

        // 3. Crear Paciente
        Paciente paciente = new Paciente();
        paciente.setUsuario(usuario);
        paciente.setNombres(dto.getNombres());
        paciente.setApellidos(dto.getApellidos());
        paciente.setDni(dto.getDni());
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        paciente.setTelefono(dto.getTelefono());
        paciente.setDireccion(dto.getDireccion());
        paciente.setGenero(dto.getGenero());

        // Guardar
        pacienteRepository.save(paciente);
    }
}