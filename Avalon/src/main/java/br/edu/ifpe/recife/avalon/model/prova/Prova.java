/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.prova;

import br.edu.ifpe.recife.avalon.model.avaliacao.Avaliacao;
import br.edu.ifpe.recife.avalon.model.questao.Questao;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

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
                            + "AND pa.aluno.id = :idAluno AND pa.dataHoraFim IS NOT NULL) IS NULL"
            ),
            @NamedQuery(
                    name = "Prova.PorProfessor",
                    query = "Select p from Prova p where p.ativa = true "
                            + "AND p.professor.email = :emailProfessor"
            ),
            @NamedQuery(
                    name = "Prova.PorId",
                    query = "Select p from Prova p where p.ativa = true "
                            + "AND :idProva = p.id"
            )
        }
)
public class Prova extends Avaliacao {

    @NotNull(message = "{prova.data.hora.fim.obrigatoria}")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DH_INICIO")
    private Date dataHoraInicio;
    
    @NotNull(message = "{prova.data.hora.fim.obrigatoria}")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DH_FIM")
    private Date dataHoraFim;
    
    @Transient
    private Long duracao;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TB_QUESTAO_PROVA", joinColumns = {
        @JoinColumn(name = "ID_AVALIACAO")},
            inverseJoinColumns = {
                @JoinColumn(name = "ID_QUESTAO")
            })
    private List<Questao> questoes;

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

    public List<Questao> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(List<Questao> questoes) {
        this.questoes = questoes;
    }

    public Long getDuracao() {
        if(dataHoraInicio != null && dataHoraFim != null){
            duracao = TimeUnit.MILLISECONDS.toMinutes(dataHoraFim.getTime() - dataHoraInicio.getTime()); 
        }
        
        return duracao;
    }
    
}
