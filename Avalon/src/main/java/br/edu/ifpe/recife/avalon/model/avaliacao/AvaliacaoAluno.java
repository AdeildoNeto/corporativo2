/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.avaliacao;

import br.edu.ifpe.recife.avalon.model.questao.MultiplaEscolha;
import br.edu.ifpe.recife.avalon.model.questao.VerdadeiroFalso;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 *
 * @author eduardoamaral
 */
@MappedSuperclass
public class AvaliacaoAluno implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_AVALIACAO_ALUNO")
    private Long id;

    @NotNull(message = "{aluno.obrigatorio}")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
    private Usuario aluno;

    @NotNull(message = "{prova.data.hora.inico.obrigatoria}")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DH_INICIO")
    private Date dataHoraInicio;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DH_FIM")
    private Date dataHoraFim;

    @Transient
    private Double nota;

    /**
     * Calcula a nota obtida pelo aluno em uma avaliação.
     *
     * @param questoesAvaliacao
     */
    public void calcularNota(List<QuestaoAlunoAvalicao> questoesAvaliacao) {
        double respostasCertas = 0.0;
        int pesoQuestoes = 0;

        for (QuestaoAlunoAvalicao questaoAlunoAvalicao : questoesAvaliacao) {
            pesoQuestoes += questaoAlunoAvalicao.getQuestaoAvaliacao().getPeso();
            if (questaoAlunoAvalicao.getQuestaoAvaliacao().getQuestao() instanceof VerdadeiroFalso) {
                respostasCertas += verificarAcertoVF(questaoAlunoAvalicao);
            } else {
                respostasCertas += verificarAcertoME(questaoAlunoAvalicao);
            }
        }

        nota = (respostasCertas / pesoQuestoes) * 10.0;
    }

    /**
     * Verifica se o aluno acertou uma questão de verdadeiro ou falso.
     *
     * @param questaoAlunoAvalicao
     * @return quantidade de pontos
     */
    private int verificarAcertoVF(QuestaoAlunoAvalicao questaoAlunoAvalicao) {
        int pontos = 0;

        VerdadeiroFalso questaoVF = (VerdadeiroFalso) questaoAlunoAvalicao.getQuestaoAvaliacao().getQuestao();
        if (questaoVF.isAnulada()
                || questaoVF.getResposta().equals(questaoAlunoAvalicao.getRespostaVF())) {
            pontos = questaoAlunoAvalicao.getQuestaoAvaliacao().getPeso();
        }

        return pontos;
    }

    /**
     * Verifica se o aluno acertou uma questão de múltipla escolha.
     * 
     * @param questaoAlunoAvalicao
     * @return 
     */
    private int verificarAcertoME(QuestaoAlunoAvalicao questaoAlunoAvalicao) {
        int pontos = 0;

        MultiplaEscolha questaoMS = (MultiplaEscolha) questaoAlunoAvalicao.getQuestaoAvaliacao().getQuestao();
        if (questaoMS.isAnulada()
                || questaoMS.getOpcaoCorreta().equals(questaoAlunoAvalicao.getRespostaMultiplaEscolha())) {
            pontos = questaoAlunoAvalicao.getQuestaoAvaliacao().getPeso();
        }
        
        return pontos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getAluno() {
        return aluno;
    }

    public void setAluno(Usuario aluno) {
        this.aluno = aluno;
    }

    public Date getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(Date dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public Date getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(Date dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

}
