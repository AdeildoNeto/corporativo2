/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.prova;

import br.edu.ifpe.recife.avalon.model.questao.MultiplaEscolha;
import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.VerdadeiroFalso;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class ProvaAlunoQuestao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PROVA_QUESTAO_ALUNO")
    private Long id;

    @NotNull(message = "{questao.obrigatoria}")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_QUESTAO", referencedColumnName = "ID_QUESTAO")
    private Questao questao;

    @Column(name = "SN_RESPOSTA_VF")
    private Boolean respostaVF;
    
    @Column(name = "IDX_RESPOSTA_MULTIPLA_ESCOLHA")
    private Integer respostaMultiplaEscolha;
    
    @NotNull(message = "{prova.obrigatoria}")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_PROVA_ALUNO", referencedColumnName = "ID_PROVA_ALUNO")
    private ProvaAluno provaAluno;

    public boolean getRespostaCorreta(){
        return ((VerdadeiroFalso) questao).getResposta();
    }
    
    public Integer getAlternativaCorreta(){
        return ((MultiplaEscolha) questao).getOpcaoCorreta();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Questao getQuestao() {
        return questao;
    }

    public void setQuestao(Questao questao) {
        this.questao = questao;
    }

    public ProvaAluno getProvaAluno() {
        return provaAluno;
    }

    public void setProvaAluno(ProvaAluno provaAluno) {
        this.provaAluno = provaAluno;
    }

    public Boolean getRespostaVF() {
        return respostaVF;
    }

    public void setRespostaVF(Boolean respostaVF) {
        this.respostaVF = respostaVF;
    }

    public Integer getRespostaMultiplaEscolha() {
        return respostaMultiplaEscolha;
    }

    public void setRespostaMultiplaEscolha(Integer respostaMultiplaEscolha) {
        this.respostaMultiplaEscolha = respostaMultiplaEscolha;
    }
    
}
