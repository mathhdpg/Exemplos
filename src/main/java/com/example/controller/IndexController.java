package com.example.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Telefone;
import com.example.model.Usuario;
import com.example.repository.CustomUsuarioRepository;
import com.example.repository.UsuarioRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class IndexController {
    
    private UsuarioRepository repository;
    private CustomUsuarioRepository customRepository;

    public IndexController(
                UsuarioRepository repository,
                CustomUsuarioRepository customRepository ) {
        this.repository = repository;
        this.customRepository = customRepository;
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

    @GetMapping("/usuarios/all")
    public ResponseEntity<Page<Usuario>> listaUsuarios(Pageable pageable) {
        try {
            Page<Usuario> usuarios = repository.findAll(pageable);
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/usuarios/map/all")
    public ResponseEntity<Page<Map<String, Object>>> listaUsuariosMap(Pageable pageable) {
        try {
            Page<Map<String, Object>> usuarios = customRepository.listaGenerica(pageable);
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/usuarios/map/all")
    public ResponseEntity<Page<Map<String, Object>>> postListaUsuariosMap(@RequestBody String sql, Pageable pageable) {
        // System.out.println(sql);
        try {
            Page<Map<String, Object>> usuarios = customRepository.listaGenerica(sql, pageable);
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
