package com.example.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.repository.CustomUsuarioRepository;
import com.example.repository.UsuarioRepository;

@RestController
@RequestMapping("/api")
public class IndexController {
    
    private CustomUsuarioRepository customUsuarioRepository;

    public IndexController(CustomUsuarioRepository customUsuarioRepository ) {
        this.customUsuarioRepository = customUsuarioRepository;
    }

    public static void main(String[] args) {
        for (int i = 132; i < 275; i++) {
            System.out.println("INSERT INTO endereco (rua, cidade, usuario_id) VALUES ('Rua A, 123', 'Cidade 1', " + i + ");");
            System.out.println("INSERT INTO endereco (rua, cidade, usuario_id) VALUES ('Rua A, 123', 'Cidade 2', " + i + ");");
            System.out.println("INSERT INTO endereco (rua, cidade, usuario_id) VALUES ('Rua A, 123', 'Cidade 3', " + i + ");");
            System.out.println("INSERT INTO endereco (rua, cidade, usuario_id) VALUES ('Rua A, 123', 'Cidade 1', " + i + ");");
            System.out.println("INSERT INTO endereco (rua, cidade, usuario_id) VALUES ('Rua A, 123', 'Cidade 2', " + i + ");");
            System.out.println("INSERT INTO endereco (rua, cidade, usuario_id) VALUES ('Rua A, 123', 'Cidade 3', " + i + ");");
            System.out.println("INSERT INTO telefone (numero, usuario_id) VALUES ('1111111111', " + i + ");");
            System.out.println("INSERT INTO telefone (numero, usuario_id) VALUES ('1111111111', " + i + ");");
            System.out.println("INSERT INTO telefone (numero, usuario_id) VALUES ('1111111111', " + i + ");");
            System.out.println("INSERT INTO telefone (numero, usuario_id) VALUES ('1111111111', " + i + ");");
            System.out.println("INSERT INTO telefone (numero, usuario_id) VALUES ('1111111111', " + i + ");");
            System.out.println("INSERT INTO telefone (numero, usuario_id) VALUES ('1111111111', " + i + ");");
            System.out.println("INSERT INTO telefone (numero, usuario_id) VALUES ('1111111111', " + i + ");");
            System.out.println("INSERT INTO telefone (numero, usuario_id) VALUES ('1111111111', " + i + ");");
        }
    }


    @GetMapping("/usuarios/map/all")
    public ResponseEntity<Page<Map<String, Object>>> listaUsuariosMap(Pageable pageable) {
        try {
            Page<Map<String, Object>> usuarios = customUsuarioRepository.listaGenerica(pageable);
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
