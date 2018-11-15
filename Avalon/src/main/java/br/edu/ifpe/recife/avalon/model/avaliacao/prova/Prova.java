/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.avaliacao.prova;

import br.edu.ifpe.recife.avalon.model.avaliacao.Avaliacao;
import br.edu.ifpe.recife.avalon.model.turma.Turma;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
                    query = "Select p from Prova p where p.ativa = true AND p.turma IS NULL "
                            + "AND :dataHoraAtual BETWEEN p.dataHoraInicio AND p.dataHoraFim "
                            + "AND (SELECT pa.id FROM ProvaAluno pa WHERE p.id = pa.prova.id "
                            + "AND pa.aluno = :aluno AND pa.dataHoraFim IS NOT NULL) IS NULL"      
            ),
            @NamedQuery(
                    name = "Prova.PorTurmaDisponibilidade",
                    query = "Select p from Prova p where p.ativa = true AND :aluno MEMBER OF p.turma.alunos "
                            + "AND :dataHoraAtual BETWEEN p.dataHoraInicio AND p.dataHoraFim "
                            + "AND (SELECT pa.id FROM ProvaAluno pa WHERE p.id = pa.prova.id "
                            + "AND pa.aluno = :aluno AND pa.dataHoraFim IS NOT NULL) IS NULL "
            ),
            @NamedQuery(
                    name = "Prova.PorProfessor",
                    query = "Select p from Prova p where p.ativa = true "
                            + "AND p.professor = :professor"
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
    
    @Column(name = "SN_LIBERAR_RESULTADO")
    private boolean liberarResultado = true;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "ID_TURMA", referencedColumnName = "ID_TURMA", nullable = true)
    private Turma turma;
    
    @Transient
    private Long duracao;

    @OneToMany(mappedBy = "prova", fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<QuestaoProva> questoes;

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

    public List<QuestaoProva> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(List<QuestaoProva> questoes) {
        this.questoes = questoes;
    }

    public boolean isLiberarResultado() {
        return liberarResultado;
    }

    public void setLiberarResultado(boolean liberarResultado) {
        this.liberarResultado = liberarResultado;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }
    
    public Long getDuracao() {
        if(dataHoraInicio != null && dataHoraFim != null){
            duracao = TimeUnit.MILLISECONDS.toMinutes(dataHoraFim.getTime() - dataHoraInicio.getTime()); 
        }
        
        return duracao;
    }
    
}
