package com.hospitalsanjose.hospitalbackend.config;

import com.hospitalsanjose.hospitalbackend.repository.UsuarioRepository;
import com.hospitalsanjose.hospitalbackend.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UsuarioRepository usuarioRepository) {
        return username -> usuarioRepository.findByCorreo(username)
                .map(u -> new User(
                        u.getCorreo(),
                        u.getPasswordHash(),
                        u.getActivo(),
                        true, true, true,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + u.getRol().getNombreRol()))
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }

    // Cadena 1: API REST (/api/**) -> Protegida con JWT
    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        http
            .securityMatcher("/api/**")
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // PERMITIMOS ACCESO LIBRE A AUTH, CHATBOT, CITAS Y MÉDICOS
                .requestMatchers("/api/auth/**", "/api/chatbot/**", "/api/citas/**", "/api/medicos/**").permitAll()
                // NUEVO (Sprint 3): mismos criterios de acceso que el resto de la API
                // (sin token JWT obligatorio), para no romper las llamadas fetch()
                // ya existentes desde las vistas Thymeleaf.
                .requestMatchers("/api/historia-clinica/**", "/api/especialidades/**",
                        "/api/consultorios/**", "/api/pacientes/**", "/api/reportes/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Cadena 2: Vistas Web (Formulario de Login -> Dashboard)
    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/registro", "/css/**", "/js/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                // NUEVO (Sprint 3): antes todo usuario caía en /dashboard sin
                // importar su rol (un Médico o Administrador veían el panel
                // de Paciente, lo cual no tenía sentido). Ahora se redirige
                // según el rol. Para "Paciente" el destino sigue siendo
                // exactamente "/dashboard", igual que en Sprint 1 y 2: cero
                // cambio de comportamiento para ese flujo ya probado.
                .successHandler((request, response, authentication) -> {
                    String rol = authentication.getAuthorities().stream()
                            .findFirst()
                            .map(a -> a.getAuthority())
                            .orElse("");

                    String destino = "/dashboard";
                    if ("ROLE_Administrador".equals(rol)) {
                        destino = "/admin";
                    } else if ("ROLE_Medico".equals(rol)) {
                        destino = "/historia-clinica";
                    }
                    response.sendRedirect(destino);
                })
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}