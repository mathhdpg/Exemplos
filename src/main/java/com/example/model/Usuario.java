package com.example.model;


import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "usuario")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;

    private EnumTipoPessoa tipoPessoa;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "acesso_id", referencedColumnName = "id")
    private UsuarioAcesso acesso;
    
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Endereco> enderecos;
    
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Telefone> telefones;

    public Usuario() {
        this.tipoPessoa = EnumTipoPessoa.FISICA;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    public List<Telefone> getTelefones() {
        return telefones;
    }

    public void setTelefones(List<Telefone> telefones) {
        this.telefones = telefones;
    }

    public EnumTipoPessoa getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(EnumTipoPessoa tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public UsuarioAcesso getAcesso() {
        return acesso;
    }

    public void setAcesso(UsuarioAcesso acesso) {
        this.acesso = acesso;
    }



    public Long getIdVezes10() {
        return id * 10;
    }
    
}
