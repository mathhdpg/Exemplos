package com.example.controller;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tomcat.util.file.ConfigurationSource.Resource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.repository.CustomRepository;

@RestController
@RequestMapping("/api/queryExecutor")
public class QueryExecutorController {
    
    private CustomRepository customRepository;

    public QueryExecutorController(
                CustomRepository customRepository ) {
        this.customRepository = customRepository;
    }
    
    @PostMapping("/executeQuery")
    public ResponseEntity<Page<Map<String, Object>>> executeQuery(@RequestBody String sql, Pageable pageable) {
        try {
            Page<Map<String, Object>> resultados = customRepository.listaGenerica(sql, pageable);
            
            StringBuffer sb = new StringBuffer("");
            sb.append(sql);

            return new ResponseEntity<>(resultados, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping(value = "/executeQuery/csv", produces = "text/csv")
    public ResponseEntity<InputStreamResource> executeQuery(@RequestBody String sql) {
        try {
            StringBuffer sb = new StringBuffer();
            List<Map<String, Object>> resultados = customRepository.listaGenerica(sql);
            if (resultados.isEmpty()) throw new Exception("Nenhum resultado encontrado!");
            Set<String> keys = resultados.get(0).keySet();
            keys.stream().forEach(key -> sb.append(key + ";"));
            for (Map<String,Object> map : resultados) {
                sb.append("\r\n");
                keys.stream().forEach(key -> sb.append("\"" + map.get(key).toString().replace("\"", "\\\"") + "\";"));
            }
            byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
            InputStreamResource is = new InputStreamResource(new ByteArrayInputStream(bytes));

            String csvFileName = "people.csv";

            // setting HTTP headers
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + csvFileName);
            // defining the custom Content-Type
            headers.set(HttpHeaders.CONTENT_TYPE, "text/csv");
        
            return new ResponseEntity<>(
                    is,
                    headers,
                    HttpStatus.OK
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}