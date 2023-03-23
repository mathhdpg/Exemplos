package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    
}