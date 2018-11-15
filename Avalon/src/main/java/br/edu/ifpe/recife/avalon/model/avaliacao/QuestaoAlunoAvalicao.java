/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.avaliacao;

import br.edu.ifpe.recife.avalon.model.questao.Alternativa;
import br.edu.ifpe.recife.avalon.model.questao.MultiplaEscolha;
import br.edu.ifpe.recife.avalon.model.questao.VerdadeiroFalso;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

/**
 *
 * @author eduardoamaral
 */
@MappedSuperclass
public class QuestaoAlunoAvalicao implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotNull(message = "{questao.obrigatoria}")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_QUESTAO_AVALIACAO", referencedColumnName = "ID_QUESTAO_AVALIACAO")
    private QuestaoAvaliacao questaoAvaliacao;

    @Column(name = "SN_RESPOSTA_VF")
    private Boolean respostaVF;
    
    @Column(name = "IDX_RESPOSTA_MULTIPLA_ESCOLHA")
    private Integer respostaMultiplaEscolha;
    
    public boolean getRespostaCorreta(){
        return ((VerdadeiroFalso) questaoAvaliacao.getQuestao()).getResposta();
    }
    
    public char getAlternativaCorreta(){
        return (char) (65 + ((MultiplaEscolha) questaoAvaliacao.getQuestao()).getAlternativaCorreta());
    }
    
    public List<Alternativa> getAlternativas(){
        return ((MultiplaEscolha) questaoAvaliacao.getQuestao()).getAlternativas();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuestaoAvaliacao getQuestaoAvaliacao() {
        return questaoAvaliacao;
    }

    public void setQuestaoAvaliacao(QuestaoAvaliacao questaoAvaliacao) {
        this.questaoAvaliacao = questaoAvaliacao;
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
