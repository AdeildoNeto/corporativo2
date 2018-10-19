/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.prova;

import br.edu.ifpe.recife.avalon.model.avaliacao.QuestaoAvalicao;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author eduardoamaral
 */
@Entity
@Table(name = "TB_PROVA_ALUNO_QUESTAO")
public class ProvaAlunoQuestao extends QuestaoAvalicao {

    @NotNull(message = "{prova.obrigatoria}")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_AVALIACAO_ALUNO", referencedColumnName = "ID_AVALIACAO_ALUNO")
    private ProvaAluno provaAluno;

    public ProvaAluno getProvaAluno() {
        return provaAluno;
    }

    public void setProvaAluno(ProvaAluno provaAluno) {
        this.provaAluno = provaAluno;
    }

}
