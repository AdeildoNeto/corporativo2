/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.simulado;

import br.edu.ifpe.recife.avalon.model.prova.*;
import br.edu.ifpe.recife.avalon.model.questao.Alternativa;
import br.edu.ifpe.recife.avalon.model.questao.MultiplaEscolha;
import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.VerdadeiroFalso;
import java.io.Serializable;
import java.util.List;
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
@Table(name = "TB_SIMULADO_ALUNO_QUESTAO")
public class SimuladoAlunoQuestao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SIMULADO_QUESTAO_ALUNO")
    private Long id;

    @NotNull(message = "{questao.obrigatoria}")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_QUESTAO", referencedColumnName = "ID_QUESTAO")
    private Questao questao;

    @Column(name = "SN_RESPOSTA_VF")
    private Boolean respostaVF;
    
    @Column(name = "IDX_RESPOSTA_MULTIPLA_ESCOLHA")
    private Integer respostaMultiplaEscolha;
    
    @NotNull(message = "{simulado.obrigatorio}")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_SIMULADO_ALUNO", referencedColumnName = "ID_SIMULADO_ALUNO")
    private SimuladoAluno simuladoAluno;

    public boolean getRespostaCorreta(){
        return ((VerdadeiroFalso) questao).getResposta();
    }
    
    public char getAlternativaCorreta(){
        return (char) (65 + ((MultiplaEscolha) questao).getOpcaoCorreta());
    }
    
    public List<Alternativa> getAlternativas(){
        return ((MultiplaEscolha) questao).getAlternativas();
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

    public SimuladoAluno getSimuladoAluno() {
        return simuladoAluno;
    }

    public void setSimuladoAluno(SimuladoAluno simuladoAluno) {
        this.simuladoAluno = simuladoAluno;
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
