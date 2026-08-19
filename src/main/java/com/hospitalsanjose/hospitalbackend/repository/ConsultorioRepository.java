package com.hospitalsanjose.hospitalbackend.repository;

import com.hospitalsanjose.hospitalbackend.model.Consultorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultorioRepository extends JpaRepository<Consultorio, Integer> {
}