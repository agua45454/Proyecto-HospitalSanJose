package com.hospitalsanjose.hospitalbackend.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String correo;
    private String password;
}