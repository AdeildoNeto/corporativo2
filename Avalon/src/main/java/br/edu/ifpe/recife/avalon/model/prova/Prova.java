/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.prova;

import br.edu.ifpe.recife.avalon.model.questao.ComponenteCurricular;
import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
@Table(name = "TB_PROVA")
@NamedQueries(
        {
            @NamedQuery(
                    name = "Prova.PorDisponibilidade",
                    query = "Select p from Prova p where p.ativa = true "
                            + "AND :dataHoraAtual BETWEEN p.dataHoraInicio AND p.dataHoraFim "
                            + "AND (SELECT pa.id FROM ProvaAluno pa WHERE p.id = pa.prova.id "
                            + "AND pa.aluno.id = :idAluno) IS NULL"
            ),
            @NamedQuery(
                    name = "Prova.PorProfessor",
                    query = "Select p from Prova p where p.ativa = true "
                            + "AND p.professor.email = :emailProfessor"
            )
        }
)
public class Prova implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PROVA")
    private Long id;

    @NotNull(message = "{prova.titulo.obrigatorio}")
    @Size(max = 40, message = "{titulo.tamanho.maximo}")
    @Column(name = "TXT_TITULO")
    private String titulo;

    @NotNull(message = "{criador.obrigatorio}")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
    private Usuario professor;

    @NotNull(message = "{componente.curricular.obrigatorio}")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_COMPONENTE_CURRICULAR", referencedColumnName = "ID")
    private ComponenteCurricular componenteCurricular;

    @NotNull(message = "{prova.data.criacao.obrigatoria}")
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_CRIACAO")
    private Date dataCriacao;
    
    @NotNull(message = "{prova.data.hora.fim.obrigatoria}")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DH_INICIO")
    private Date dataHoraInicio;
    
    @NotNull(message = "{prova.data.hora.fim.obrigatoria}")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DH_FIM")
    private Date dataHoraFim;
    
    @NotNull(message = "{prova.duracao.obrigatoria}")
    @Column(name = "NUM_DURACAO")
    private Long duracao;

    @Column(name = "SN_ATIVA", nullable = false)
    private Boolean ativa = true;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TB_QUESTOES_PROVA", joinColumns = {
        @JoinColumn(name = "ID_PROVA")},
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

    public Usuario getProfessor() {
        return professor;
    }

    public void setProfessor(Usuario professor) {
        this.professor = professor;
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

    public Date getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(Date dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public Date getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(Date dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public List<Questao> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(List<Questao> questoes) {
        this.questoes = questoes;
    }

    public Long getDuracao() {
        return duracao;
    }

    public void setDuracao(Long duracao) {
        this.duracao = duracao;
    }
    
}
