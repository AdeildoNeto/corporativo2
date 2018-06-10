/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.prova;

import br.edu.ifpe.recife.avalon.model.questao.Questao;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
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
    @Column(name = "ID_PROVA_ALUNO")
    private Long id;

    @NotNull(message = "{questao.obrigatoria}")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
    private Questao questao;

    @NotNull(message = "{prova.obrigatoria}")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_PROVA", referencedColumnName = "ID")
    private Prova provaAluno;
    
    @Column(name = "SN_RESPOSTA_VF")
    private Boolean respostaVF;
    
    @Column(name = "IDX_RESPOSTA_MULTIPLA_ESCOLHA")
    private Integer respostaMultiplaEscolha;
    
    @ManyToMany(mappedBy = "questoesAluno")
    private List<ProvaAluno> provasAluno;

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

    public Prova getProvaAluno() {
        return provaAluno;
    }

    public void setProvaAluno(Prova provaAluno) {
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

    public List<ProvaAluno> getProvasAluno() {
        return provasAluno;
    }

    public void setProvasAluno(List<ProvaAluno> provasAluno) {
        this.provasAluno = provasAluno;
    }
    
}
