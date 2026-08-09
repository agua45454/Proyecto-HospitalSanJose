package com.hospitalsanjose.hospitalbackend.service;

import com.hospitalsanjose.hospitalbackend.dto.RegistroMedicoDTO;
import com.hospitalsanjose.hospitalbackend.model.Medico;
import com.hospitalsanjose.hospitalbackend.model.Rol;
import com.hospitalsanjose.hospitalbackend.model.Usuario;
import com.hospitalsanjose.hospitalbackend.repository.MedicoRepository;
import com.hospitalsanjose.hospitalbackend.repository.RolRepository;
import com.hospitalsanjose.hospitalbackend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void registrarMedico(RegistroMedicoDTO dto) {
        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new RuntimeException("El correo ya se encuentra registrado.");
        }

        if (medicoRepository.existsByColegiatura(dto.getColegiatura())) {
            throw new RuntimeException("El número de colegiatura ya se encuentra registrado.");
        }

        Rol rolMedico = rolRepository.findByNombreRol("Medico")
                .orElseThrow(() -> new RuntimeException("Error: El rol 'Medico' no existe en la base de datos."));

        Usuario usuario = new Usuario();
        usuario.setCorreo(dto.getCorreo());
        usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol(rolMedico);

        Medico medico = new Medico();
        medico.setUsuario(usuario);
        medico.setNombres(dto.getNombres());
        medico.setApellidos(dto.getApellidos());
        medico.setColegiatura(dto.getColegiatura());
        medico.setEspecialidad(dto.getEspecialidad());
        medico.setTelefono(dto.getTelefono());

        medicoRepository.save(medico);
    }
}