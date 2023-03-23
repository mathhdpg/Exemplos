package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.Telefone;

public interface TelefoneRepository extends JpaRepository<Telefone, Long> {
    
}