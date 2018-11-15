/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.avaliacao.simulado;

import br.edu.ifpe.recife.avalon.model.avaliacao.Avaliacao;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author eduardoamaral
 */
@Entity
@Table(name = "TB_SIMULADO")
@NamedQueries(
        {
            @NamedQuery(
                    name = "Simulado.PorTituloValido",
                    query = "Select s from Simulado s where s.ativa = true"
                    + " AND s.titulo = :titulo"
                    + " AND s.componenteCurricular = :componenteCurricular"
                    + " AND s.professor = :professor"
            )
            ,@NamedQuery(
                    name = "Simulado.PorProfessor",
                    query = "Select s from Simulado s where s.ativa = true"
                    + " AND s.professor = :professor"
            )
            ,@NamedQuery(
                    name = "Simulado.PorFiltro",
                    query = "Select s from Simulado s where s.ativa = true "
                    + "AND (:idComponenteCurricular is null OR :idComponenteCurricular = s.componenteCurricular.id) "
                    + "AND (:nomeProfessor is null OR (CONCAT(s.professor.nome, ' ', s.professor.sobrenome) like :nomeProfessor)) "
                    + "AND (:titulo is null OR s.titulo like :titulo)"
            )
            ,@NamedQuery(
                    name = "Simulado.PorId",
                    query = "Select s from Simulado s where s.ativa = true "
                    + "AND :idSimulado = s.id"
            )
        }
)
public class Simulado extends Avaliacao {

    @OneToMany(mappedBy = "simulado", fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<QuestaoSimulado> questoes;

    public List<QuestaoSimulado> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(List<QuestaoSimulado> questoes) {
        this.questoes = questoes;
    }

}
