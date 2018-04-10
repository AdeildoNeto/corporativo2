/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.questao;

import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author eduardo.f.amaral
 */
@Entity
@Table(name = "TB_QUESTAO")
@NamedQueries(
        {
            @NamedQuery(
                    name = "Questao.PorId",
                    query = "SELECT q FROM Questao q WHERE q.id = :id"
            )
            ,@NamedQuery(
                    name = "Questao.PorAutor",
                    query = "SELECT q FROM Questao q WHERE q.autor.id = :idAutor"
            )
            ,@NamedQuery(
                    name = "Questao.PorTipo",
                    query = "SELECT q FROM Questao q WHERE q.tipo = :tipo"
            )
            ,@NamedQuery(
                    name = "Questao.PorAutorTipo",
                    query = "SELECT q FROM Questao q WHERE q.tipo = :tipo AND q.autor.id = :idAutor"
            )
        })
public class Questao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "TXT_ENUNCIADO")
    private String enunciado;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
    private Usuario autor;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoQuestaoEnum tipo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public TipoQuestaoEnum getTipo() {
        return tipo;
    }

    public void setTipo(TipoQuestaoEnum tipo) {
        this.tipo = tipo;
    }

}
