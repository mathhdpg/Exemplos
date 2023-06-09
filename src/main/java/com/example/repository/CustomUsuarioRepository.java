package com.example.repository;

import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class CustomUsuarioRepository extends CustomRepository {

    public CustomUsuarioRepository(EntityManager entityManager) {
        super(entityManager);
        
    }
    public Page<Map<String, Object>> listaGenerica(Pageable pageable) {
        String sql = ""
        + "select usuario.id as id,"
        + "       usuario.nome as nome"
        + "  from usuario";

        return this.executeNativeQuery(sql, pageable);
    }
}