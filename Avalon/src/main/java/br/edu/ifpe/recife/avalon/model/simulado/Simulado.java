/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.simulado;

import br.edu.ifpe.recife.avalon.model.avaliacao.Avaliacao;
import br.edu.ifpe.recife.avalon.model.questao.Questao;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
                    query = "Select s from Simulado s where s.ativo = true"
                    + " AND s.titulo = :titulo"
                    + " AND s.componenteCurricular.id = :idComponenteCurricular"
                    + " AND s.professor.email = :emailProfessor"
            )
            ,@NamedQuery(
                    name = "Simulado.PorProfessor",
                    query = "Select s from Simulado s where s.ativo = true"
                    + " AND s.professor.email = :emailProfessor"
            )
            ,@NamedQuery(
                    name = "Simulado.PorFiltro",
                    query = "Select s from Simulado s where s.ativo = true "
                    + "AND (:idComponenteCurricular is null OR :idComponenteCurricular = s.componenteCurricular.id) "
                    + "AND (:nomeProfessor is null OR (CONCAT(s.professor.nome, ' ', s.professor.sobrenome) like :nomeProfessor)) "
                    + "AND (:titulo is null OR s.titulo like :titulo)"
            )
            ,@NamedQuery(
                    name = "Simulado.PorId",
                    query = "Select s from Simulado s where s.ativo = true "
                    + "AND :idSimulado = s.id"
            )
        }
)
public class Simulado extends Avaliacao {

    @Column(name = "SN_ATIVO", nullable = false)
    private Boolean ativo = true;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TB_QUESTAO_SIMULADO", joinColumns = {
        @JoinColumn(name = "ID_AVALIACAO")},
            inverseJoinColumns = {
                @JoinColumn(name = "ID_QUESTAO")
            })
    private List<Questao> questoes;

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
