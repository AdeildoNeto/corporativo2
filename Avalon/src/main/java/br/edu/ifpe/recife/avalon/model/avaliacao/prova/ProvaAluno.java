/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.avaliacao.prova;

import br.edu.ifpe.recife.avalon.model.avaliacao.AvaliacaoAluno;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author eduardoamaral
 */
@Entity
@Table(name = "TB_PROVA_ALUNO")
@NamedQueries(
        {
            @NamedQuery(
                    name = "ProvaAluno.PorResultadoAluno",
                    query = "Select pa from ProvaAluno pa where pa.aluno.id = :idAluno"
                    + " AND (pa.prova.liberarResultado = true AND :dataAtual > pa.prova.dataHoraFim)"
            )
            ,
            @NamedQuery(
                    name = "ProvaAluno.PorProva",
                    query = "Select pa from ProvaAluno pa where pa.prova.id = :idProva "
                    + "ORDER BY pa.aluno.nome, pa.aluno.sobrenome"
            )
            ,
            @NamedQuery(
                    name = "ProvaAluno.PorAlunoProva",
                    query = "Select pa from ProvaAluno pa where pa.prova.id = :idProva "
                    + "AND pa.aluno.id = :idAluno"
            )
        }
)
public class ProvaAluno extends AvaliacaoAluno {

    @NotNull(message = "{prova.obrigatoria}")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_AVALIACAO", referencedColumnName = "ID_AVALIACAO")
    private Prova prova;

    @OneToMany(mappedBy = "provaAluno", fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<QuestaoAlunoProva> questoesAluno;

    private void calcularNota() {
        super.setNota(0.0);

        if (questoesAluno != null && !questoesAluno.isEmpty()) {
            super.calcularNota(new ArrayList(questoesAluno), prova.getNotaMaxima());
        }
    }

    public Prova getProva() {
        return prova;
    }

    public void setProva(Prova prova) {
        this.prova = prova;
    }

    public List<QuestaoAlunoProva> getQuestoesAluno() {
        return questoesAluno;
    }

    public void setQuestoesAluno(List<QuestaoAlunoProva> questoesAluno) {
        this.questoesAluno = questoesAluno;
    }

    @Override
    public Double getNota() {
        calcularNota();
        return super.getNota();
    }

}
