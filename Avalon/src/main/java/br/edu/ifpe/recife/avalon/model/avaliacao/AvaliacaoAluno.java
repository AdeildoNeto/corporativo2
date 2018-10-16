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
     * @param questoesAvaliacao
     */
    public void calcularNota(List<QuestaoAvalicao> questoesAvaliacao) {
        double respostasCertas = 0.0;

        for (QuestaoAvalicao questaoAvaliacao : questoesAvaliacao) {
            if (questaoAvaliacao.getQuestao() instanceof VerdadeiroFalso) {
                VerdadeiroFalso questaoVF = (VerdadeiroFalso) questaoAvaliacao.getQuestao();
                if (questaoVF.isAnulada()
                        || questaoVF.getResposta().equals(questaoAvaliacao.getRespostaVF())) {
                    respostasCertas++;
                }
            } else {
                MultiplaEscolha questaoMS = (MultiplaEscolha) questaoAvaliacao.getQuestao();
                if (questaoMS.isAnulada()
                        || questaoMS.getOpcaoCorreta().equals(questaoAvaliacao.getRespostaMultiplaEscolha())) {
                    respostasCertas++;
                }
            }
        }

        nota = (respostasCertas / questoesAvaliacao.size()) * 10.0;
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
