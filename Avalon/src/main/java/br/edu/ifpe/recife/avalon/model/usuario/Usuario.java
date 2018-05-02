/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.usuario;

import br.edu.ifpe.recife.avalon.model.questao.Questao;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author eduardo.f.amaral
 */
@Entity
@Table(name = "TB_USUARIO")
@NamedQueries(
        {
            @NamedQuery(
                    name = "Usuario.PorLogin",
                    query = "Select u from Usuario u where u.email = :email and u.senha = :senha")
            ,
            @NamedQuery(
                    name = "Usuario.PorEmail",
                    query = "Select u from Usuario u where u.email = :email")
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

    @Column(name = "TXT_SENHA", columnDefinition = "varchar(2000)")
    private String senha;

    @OneToMany(mappedBy = "criador", fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Questao> questoes;

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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<Questao> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(List<Questao> questoes) {
        this.questoes = questoes;
    }

}
