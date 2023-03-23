package com.example.repository;

import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class CustomUsuarioRepository extends GenericDao {

    public CustomUsuarioRepository(EntityManager entityManager) {
        super(entityManager);
        
    }
    public Page<Map<String, Object>> listaGenerica(Pageable pageable) {
        String sql = ""
        + "select usuario.id as id,"
        + "       usuario.nome as nome"
        + "  from usuario";

        return listaGenerica(sql, pageable);
    }
    
    public Page<Map<String, Object>> listaGenerica(String sql, Pageable pageable) {
        return this.executeNativeQuery(sql, pageable);
    }
    
}