/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.questao.componente;

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
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author eduardoamaral
 */
@Entity
@Table(name = "TB_COMPONENTE_CURRICULAR")
@NamedQueries(
        {
            @NamedQuery(
                    name = "ComponenteCurricular.PorNome",
                    query = "SELECT c FROM ComponenteCurricular c WHERE c.nome = :nomeComponente"
            ),
            @NamedQuery(
                    name = "ComponenteCurricular.Todos",
                    query = "SELECT c FROM ComponenteCurricular c"
            )
        })
public class ComponenteCurricular implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    
    @NotBlank(message = "{componente.curricular.nome.obrigatorio}")
    @Size(max = 255, message = "{componente.curricular.tamanho.nome}")
    @Column(name = "TXT_NOME", unique = true)
    private String nome;
    
    @OneToMany(mappedBy = "componenteCurricular", fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST, orphanRemoval = false)
    private List<Questao> questoes;

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

    public List<Questao> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(List<Questao> questoes) {
        this.questoes = questoes;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ComponenteCurricular)) {
            return false;
        }
        ComponenteCurricular other = (ComponenteCurricular) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.ifpe.recife.avalon.model.questao.ComponenteCurricular[ id=" + id + " ]";
    }
    
}
