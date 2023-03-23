package com.example.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    /*
    @Query(""
    + " select usuario from Usuario usuario "
    + "   left join fetch usuario.telefones"
    + "   left join fetch usuario.enderecos " )
    List<Usuario> listaTodosUsuarios();
     */

    //  @EntityGraph(attributePaths = {"enderecos", "telefones"})
     List<Usuario> findAll();

     Page<Usuario> findAll(Pageable pageable);

    
}