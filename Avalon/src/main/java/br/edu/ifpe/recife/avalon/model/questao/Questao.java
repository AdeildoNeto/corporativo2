/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.questao;

import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
            ),@NamedQuery(
                    name = "Questao.PorCriador",
                    query = "SELECT q FROM Questao q WHERE q.criador.id = :idCriador"
            )
            ,@NamedQuery(
                    name = "Questao.PorTipo",
                    query = "SELECT q FROM Questao q WHERE q.tipo = :tipo"
            )
            ,@NamedQuery(
                    name = "Questao.PorCriadorTipo",
                    query = "SELECT q FROM Questao q WHERE q.tipo = :tipo AND q.criador.id = :idCriador"
            ),@NamedQuery(
                    name = "Questao.PorEnunciadoTipo",
                    query = "SELECT q FROM Questao q WHERE q.tipo = :tipo AND q.enunciado = :enunciado"
            )
        })
public class Questao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_QUESTAO")
    private Long id;

    @NotBlank(message = "{questao.enunciado.obrigatorio}")
    @Column(name = "TXT_ENUNCIADO")
    private String enunciado;

    @NotNull(message = "{questao.criador.obrigatorio}")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
    private Usuario criador;

    @NotNull(message = "{questao.tipo.obrigatorio}")
    @Enumerated(EnumType.STRING)
    @Column(name = "TXT_TIPO")
    private TipoQuestaoEnum tipo;
    
    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_CRIACAO")
    private Date dataCriacao;

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

    public Usuario getCriador() {
        return criador;
    }

    public void setCriador(Usuario criador) {
        this.criador = criador;
    }

    public TipoQuestaoEnum getTipo() {
        return tipo;
    }

    public void setTipo(TipoQuestaoEnum tipo) {
        this.tipo = tipo;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

}
