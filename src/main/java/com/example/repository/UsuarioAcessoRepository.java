package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.UsuarioAcesso;

public interface UsuarioAcessoRepository extends JpaRepository<UsuarioAcesso, Long> {
    
}