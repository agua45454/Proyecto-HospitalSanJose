package com.hospitalsanjose.hospitalbackend.service;

import com.hospitalsanjose.hospitalbackend.dto.LoginRequestDTO;
import com.hospitalsanjose.hospitalbackend.dto.LoginResponseDTO;
import com.hospitalsanjose.hospitalbackend.model.Usuario;
import com.hospitalsanjose.hospitalbackend.repository.UsuarioRepository;
import com.hospitalsanjose.hospitalbackend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public LoginResponseDTO login(LoginRequestDTO dto) {

        Usuario usuario = usuarioRepository.findByCorreo(dto.getCorreo())
                .orElseThrow(() -> new RuntimeException("Correo o contraseña incorrectos."));

        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPasswordHash())) {
            throw new RuntimeException("Correo o contraseña incorrectos.");
        }

        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw new RuntimeException("Esta cuenta se encuentra inactiva. Contacta al administrador.");
        }

        if (usuario.getRol() == null) {
            throw new RuntimeException("El usuario no tiene un rol asignado.");
        }

        String rol = usuario.getRol().getNombreRol();
        String token = jwtUtil.generarToken(usuario.getCorreo(), rol);

        return new LoginResponseDTO(token, usuario.getCorreo(), rol, usuario.getIdUsuario());
    }
}