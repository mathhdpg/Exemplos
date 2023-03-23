package com.example.controller;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Endereco;
import com.example.model.Telefone;
import com.example.model.Usuario;
import com.example.repository.EnderecoRepository;
import com.example.repository.TelefoneRepository;
import com.example.repository.UsuarioAcessoRepository;
import com.example.repository.UsuarioRepository;

@RestController
@RequestMapping("/api")
public class UsuarioController {
    
    private UsuarioRepository repository;
    private UsuarioAcessoRepository usuarioAcessoRepository;
    private EnderecoRepository enderecoRepository;
    private TelefoneRepository telefoneRepository;

    public UsuarioController(UsuarioRepository repository,
            UsuarioAcessoRepository usuarioAcessoRepository,
            EnderecoRepository enderecoRepository,
            TelefoneRepository telefoneRepository) {
        this.repository = repository;
        this.usuarioAcessoRepository = usuarioAcessoRepository;
        this.enderecoRepository = enderecoRepository;
        this.telefoneRepository = telefoneRepository;
    }

    @GetMapping("/usuario/all")
    public ResponseEntity<Page<Usuario>> listaUsuarios(Pageable pageable) {
        try {
            Page<Usuario> usuarios = repository.findAll(pageable);
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<Usuario> findById(@RequestParam("id") Long id) {
        try {
            Optional<Usuario> usuario = repository.findById(id);
            if (usuario.isPresent()) return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
            else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/usuario/")
    @Transactional
    public ResponseEntity<Usuario> insert(@RequestBody Usuario usuario) {
        try {
            if (usuario != null) {
                usuario.setId(null);
                repository.save(usuario);
                
                List<Telefone> telefones = usuario.getTelefones();
                List<Endereco> enderecos = usuario.getEnderecos();

                telefones.forEach(t -> t.setUsuario(usuario));
                enderecos.forEach(e -> e.setUsuario(usuario));

                telefoneRepository.saveAll(telefones);
                enderecoRepository.saveAll(enderecos);
                return new ResponseEntity<>(usuario, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/usuario/{id}")
    public ResponseEntity<Usuario> insert(@RequestParam("id") Long id, @RequestBody Usuario usuario) {
        try {
            if (usuario != null) {
                usuario.setId(id);
                repository.save(usuario);
                return new ResponseEntity<>(usuario, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
