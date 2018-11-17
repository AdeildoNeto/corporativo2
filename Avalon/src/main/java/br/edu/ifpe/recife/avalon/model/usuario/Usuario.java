/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.usuario;

import br.edu.ifpe.recife.avalon.model.turma.Turma;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author eduardoamaral
 */
@Entity
@Table(name = "TB_USUARIO")
@NamedQueries(
        {
            @NamedQuery(
                    name = "Usuario.PorEmail",
                    query = "Select u from Usuario u where u.email = :email"),
            @NamedQuery(
                    name = "Usuario.TodosAlunos",
                    query = "Select u from Usuario u where u.grupo = br.edu.ifpe.recife.avalon.model.usuario.GrupoEnum.ALUNO"
            )
        }
)
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotBlank
    @Email
    @Column(name = "TXT_EMAIL", unique = true)
    private String email;
    
    @NotBlank
    @Column(name = "TXT_NOME")
    private String nome;

    @NotBlank
    @Column(name = "TXT_SOBRENOME")
    private String sobrenome;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "TXT_GRUPO")
    private GrupoEnum grupo;
    
    @ManyToMany(mappedBy = "alunos")
    private List<Turma> turmas;
    
    @Transient
    private boolean selecionado = false;

    /**
     * Retorna o nome e sobrenome do usuário.
     * 
     * @return 
     */
    public String getNomeCompleto(){
        StringBuilder builder = new StringBuilder();
        builder.append(nome);
        builder.append(" ");
        builder.append(sobrenome);
        
        return builder.toString();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public GrupoEnum getGrupo() {
        return grupo;
    }

    public void setGrupo(GrupoEnum grupo) {
        this.grupo = grupo;
    }

    public List<Turma> getTurmas() {
        return turmas;
    }

    public void setTurmas(List<Turma> turmas) {
        this.turmas = turmas;
    }

    public boolean isSelecionado() {
        return selecionado;
    }

    public void setSelecionado(boolean selecionado) {
        this.selecionado = selecionado;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).build();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || obj.getClass() != this.getClass()){
            return false;
        }
        
        Usuario other = (Usuario) obj;
        return new EqualsBuilder().append(this.id, other.id).build();
    }
    
    
    
}
