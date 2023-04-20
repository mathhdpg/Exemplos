package com.example.repository;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class CustomRepository extends GenericDao {

    public CustomRepository(EntityManager entityManager) {
        super(entityManager);
        
    }
    
    public Page<Map<String, Object>> listaGenerica(String sql, Pageable pageable) {
        return this.executeNativeQuery(sql, pageable);
    }
    
    public List<Map<String, Object>> listaGenerica(String sql) {
        return this.executeNativeQuery(sql);
    }
    
}