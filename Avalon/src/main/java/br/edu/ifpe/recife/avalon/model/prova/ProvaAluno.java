/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.prova;

import br.edu.ifpe.recife.avalon.model.avaliacao.AvaliacaoAluno;
import br.edu.ifpe.recife.avalon.model.avaliacao.QuestaoAvalicao;
import br.edu.ifpe.recife.avalon.model.questao.MultiplaEscolha;
import br.edu.ifpe.recife.avalon.model.questao.VerdadeiroFalso;
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
                    + " AND :dhAtual > pa.prova.dataHoraInicio"
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
    private List<ProvaAlunoQuestao> questoesAluno;

    private void calcularNota() {
        super.setNota(0.0);

        if (questoesAluno != null && !questoesAluno.isEmpty()) {
            super.calcularNota(new ArrayList(questoesAluno));
        }
    }

    public Prova getProva() {
        return prova;
    }

    public void setProva(Prova prova) {
        this.prova = prova;
    }

    public List<ProvaAlunoQuestao> getQuestoesAluno() {
        return questoesAluno;
    }

    public void setQuestoesAluno(List<ProvaAlunoQuestao> questoesAluno) {
        this.questoesAluno = questoesAluno;
    }

    @Override
    public Double getNota() {
        calcularNota();
        return super.getNota();
    }

}
