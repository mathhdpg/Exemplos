package com.example.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.repository.CustomRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/queryExecutor")
public class QueryExecutorController {
    
    private CustomRepository customRepository;

    public QueryExecutorController(
                CustomRepository customRepository ) {
        this.customRepository = customRepository;
    }
    
    @PostMapping("/executeQuery")
    public ResponseEntity<Page<Map<String, Object>>> postListaUsuariosMap(@RequestBody String sql, Pageable pageable) {
        try {
            Page<Map<String, Object>> usuarios = customRepository.listaGenerica(sql, pageable);
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}