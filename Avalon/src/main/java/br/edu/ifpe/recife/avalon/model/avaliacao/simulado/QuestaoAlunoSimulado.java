/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.avaliacao.simulado;

import br.edu.ifpe.recife.avalon.model.avaliacao.QuestaoAlunoAvalicao;
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
@Table(name = "TB_QUESTAO_ALUNO_SIMULADO")
public class QuestaoAlunoSimulado extends QuestaoAlunoAvalicao {

    @NotNull(message = "{simulado.obrigatorio}")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_AVALIACAO_ALUNO", referencedColumnName = "ID_AVALIACAO_ALUNO")
    private SimuladoAluno simuladoAluno;

    public SimuladoAluno getSimuladoAluno() {
        return simuladoAluno;
    }

    public void setSimuladoAluno(SimuladoAluno simuladoAluno) {
        this.simuladoAluno = simuladoAluno;
    }

}
