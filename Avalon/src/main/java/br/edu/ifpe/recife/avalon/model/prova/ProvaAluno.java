/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.prova;

import br.edu.ifpe.recife.avalon.model.questao.MultiplaEscolha;
import br.edu.ifpe.recife.avalon.model.questao.VerdadeiroFalso;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 *
 * @author eduardoamaral
 */
@Entity
@Table(name = "TB_PROVA_ALUNO")
@NamedQueries(
        {
            @NamedQuery(
                    name = "ProvaAluno.PorResultadoAluno",
                    query = "Select pa from ProvaAluno pa where pa.aluno.id = :idAluno"
                    + " AND :dhAtual > pa.prova.dataHoraInicio"
            )
            ,
            @NamedQuery(
                    name = "ProvaAluno.PorProva",
                    query = "Select pa from ProvaAluno pa where pa.prova.id = :idProva "
                    + "ORDER BY pa.aluno.nome, pa.aluno.sobrenome"
            )
        }
)
public class ProvaAluno implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PROVA_ALUNO")
    private Long id;

    @NotNull(message = "{aluno.obrigatorio}")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
    private Usuario aluno;

    @NotNull(message = "{prova.obrigatoria}")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_PROVA", referencedColumnName = "ID_PROVA")
    private Prova prova;

    @NotNull(message = "{prova.data.hora.inico.obrigatoria}")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DH_INICIO")
    private Date dataHoraInicio;

    @NotNull(message = "{prova.data.hora.fim.obrigatoria}")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DH_FIM")
    private Date dataHoraFim;
    
    @Column(name = "SN_FINALIZADA", nullable = false)
    private boolean finalizada = false;

    @OneToMany(mappedBy = "provaAluno", fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<ProvaAlunoQuestao> questoesAluno;

    @Transient
    private Double nota;

    private void calcularNota() {
        nota = 0.0;
        double respostasCertas = 0.0;

        if (questoesAluno != null && !questoesAluno.isEmpty()) {
            for (ProvaAlunoQuestao provaAlunoQuestao : questoesAluno) {
                if (provaAlunoQuestao.getQuestao() instanceof VerdadeiroFalso) {
                    VerdadeiroFalso questaoVF = (VerdadeiroFalso) provaAlunoQuestao.getQuestao();
                    if (questaoVF.isAnulada()
                            || questaoVF.getResposta().equals(provaAlunoQuestao.getRespostaVF())) {
                        respostasCertas++;
                    }
                } else {
                    MultiplaEscolha questaoMS = (MultiplaEscolha) provaAlunoQuestao.getQuestao();
                    if (questaoMS.isAnulada()
                            || questaoMS.getOpcaoCorreta().equals(provaAlunoQuestao.getRespostaMultiplaEscolha())) {
                        respostasCertas++;
                    }
                }
            }
            
            nota = (respostasCertas / questoesAluno.size()) * 10.0;
        }
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

    public Prova getProva() {
        return prova;
    }

    public void setProva(Prova prova) {
        this.prova = prova;
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

    public List<ProvaAlunoQuestao> getQuestoesAluno() {
        return questoesAluno;
    }

    public void setQuestoesAluno(List<ProvaAlunoQuestao> questoesAluno) {
        this.questoesAluno = questoesAluno;
    }

    public Double getNota() {
        calcularNota();
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public boolean isFinalizada() {
        return finalizada;
    }

    public void setFinalizada(boolean finalizada) {
        this.finalizada = finalizada;
    }
    
}
