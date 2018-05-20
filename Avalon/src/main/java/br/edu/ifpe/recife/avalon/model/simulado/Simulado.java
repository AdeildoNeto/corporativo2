/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.simulado;

import br.edu.ifpe.recife.avalon.model.questao.ComponenteCurricular;
import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author eduardoamaral
 */
@Entity
@Table(name = "TB_SIMULADO")
@NamedQueries(
        {
            @NamedQuery(
                    name = "Simulado.PorDisciplina",
                    query = "Select s from Simulado s where s.componenteCurricular.id = :idComponenteCurricular AND "
                    + "s.ativo = true"
            )
            ,@NamedQuery(
                    name = "Simulado.PorTituloValido",
                    query = "Select s from Simulado s where s.titulo = :titulo AND "
                    + "s.componenteCurricular.id = :idComponenteCurricular AND "
                    + "s.criador.email = :emailCriador AND "
                    + "s.ativo = true"
            ),@NamedQuery(
                    name = "Simulado.PorCriador",
                    query = "Select s from Simulado s where s.criador.email = :emailCriador AND "
                    + "s.ativo = true"
            )
        }
)
public class Simulado implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SIMULADO")
    private Long id;

    @NotNull(message = "{simulado.titulo.obrigatorio}")
    @Size(max = 40, message = "{simulado.titulo.tamanho.maximo}")
    @Column(name = "TXT_TITULO")
    private String titulo;

    @NotNull(message = "{criador.obrigatorio}")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
    private Usuario criador;

    @NotNull(message = "{componente.curricular.obrigatorio}")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_COMPONENTE_CURRICULAR", referencedColumnName = "ID")
    private ComponenteCurricular componenteCurricular;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_CRIACAO")
    private Date dataCriacao;

    @Column(name = "SN_ATIVO", nullable = false)
    private Boolean ativo = true;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "TB_QUESTOES_SIMULADO", joinColumns = {
        @JoinColumn(name = "ID_SIMULADO")},
            inverseJoinColumns = {
                @JoinColumn(name = "ID_QUESTAO")
            })
    private List<Questao> questoes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Usuario getCriador() {
        return criador;
    }

    public void setCriador(Usuario criador) {
        this.criador = criador;
    }

    public ComponenteCurricular getComponenteCurricular() {
        return componenteCurricular;
    }

    public void setComponenteCurricular(ComponenteCurricular componenteCurricular) {
        this.componenteCurricular = componenteCurricular;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public List<Questao> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(List<Questao> questoes) {
        this.questoes = questoes;
    }
    
}
