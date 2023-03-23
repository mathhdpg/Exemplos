package com.example.repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TupleElement;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GenericDao {

    private EntityManager entityManager;

    public GenericDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Map<String, Object>> executeNativeQuery(String queryString) {
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Tuple> nativeQuery = session.createNativeQuery(queryString, Tuple.class);

        return executeNativeQuery(nativeQuery);
    }

    public Page<Map<String, Object>> executeNativeQuery(String queryString, Pageable pageable) {
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Tuple> nativeQuery = session.createNativeQuery(queryString, Tuple.class);

        nativeQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        nativeQuery.setMaxResults(pageable.getPageSize());

        List<Map<String, Object>> mapResult = executeNativeQuery(nativeQuery);

        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) FROM (" + queryString + ") AS total");
        Long count = ((Number) countQuery.getSingleResult()).longValue();

        return new PageImpl<>(mapResult, pageable, count);
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> executeNativeQuery(NativeQuery<Tuple> nativeQuery) {
        List<Tuple> resultList = nativeQuery.getResultList();
        List<Map<String, Object>> mapResult = resultList.stream().map(result -> {

            Object[] results = result.toArray();
            List<TupleElement<?>> elements = result.getElements();

            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < results.length; i++) {
                String alias = elements.get(i).getAlias();
                String[] arrayAlias = alias.split("\\.");
                Map<String, Object> coluna = map;

                for (int j = 0; j < arrayAlias.length - 1; j++) {
                    if (coluna.containsKey(arrayAlias[j])) {
                        coluna = (Map<String, Object>) coluna.get(arrayAlias[j]);
                    } else {
                        Map<String, Object> novaColuna = new HashMap<String, Object>();
                        coluna.put(arrayAlias[j], novaColuna);
                        coluna = novaColuna;
                    }
                }

                coluna.put(arrayAlias[arrayAlias.length - 1], results[i]);
            }

            return map;
        }).collect(Collectors.toList());
        return mapResult;
    }
}
