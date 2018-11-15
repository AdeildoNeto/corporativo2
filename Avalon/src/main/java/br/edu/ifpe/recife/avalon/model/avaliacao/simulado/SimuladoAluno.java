/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.avaliacao.simulado;

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
@Table(name = "TB_SIMULADO_ALUNO")
@NamedQueries(
        {
            @NamedQuery(
                    name = "SimuladoAluno.PorAluno",
                    query = "Select sa from SimuladoAluno sa where sa.aluno = :aluno"
                            + " AND sa.simulado = :simulado"
                            + " ORDER BY sa.dataHoraInicio"
            ),
            @NamedQuery(
                    name = "SimuladoAluno.PorSimulado",
                    query = "Select sa from SimuladoAluno sa where sa.simulado = :simulado"
                    + " ORDER BY sa.aluno.nome, sa.aluno.sobrenome"
            )
        }
)
public class SimuladoAluno extends AvaliacaoAluno {

    @NotNull(message = "{simulado.obrigatorio}")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_AVALIACAO", referencedColumnName = "ID_AVALIACAO")
    private Simulado simulado;

    @OneToMany(mappedBy = "simuladoAluno", fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<QuestaoAlunoSimulado> questoesAluno;

    /**
     * Calcula a nota obtida pelo aluno no simulado.
     */
    private void calcularNota() {
        super.setNota(0.0);

        if (questoesAluno != null && !questoesAluno.isEmpty()) {
            super.calcularNota(new ArrayList(questoesAluno), simulado.getNotaMaxima());
        }
    }

    public Simulado getSimulado() {
        return simulado;
    }

    public void setSimulado(Simulado simulado) {
        this.simulado = simulado;
    }

    public List<QuestaoAlunoSimulado> getQuestoesAluno() {
        return questoesAluno;
    }

    public void setQuestoesAluno(List<QuestaoAlunoSimulado> questoesAluno) {
        this.questoesAluno = questoesAluno;
    }

    @Override
    public Double getNota() {
        calcularNota();
        return super.getNota();
    }

}
