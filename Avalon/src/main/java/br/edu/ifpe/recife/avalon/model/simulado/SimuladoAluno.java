/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.simulado;

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
@Table(name = "TB_SIMULADO_ALUNO")
@NamedQueries(
        {
            @NamedQuery(
                    name = "SimuladoAluno.PorSimulado",
                    query = "Select sa from SimuladoAluno sa where sa.simulado.id = :idSimulado "
                    + "ORDER BY sa.aluno.nome, sa.aluno.sobrenome"
            )
        }
)
public class SimuladoAluno implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SIMULADO_ALUNO")
    private Long id;

    @NotNull(message = "{aluno.obrigatorio}")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
    private Usuario aluno;

    @NotNull(message = "{simulado.obrigatorio}")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_SIMULADO", referencedColumnName = "ID_SIMULADO")
    private Simulado simulado;

    @NotNull(message = "{prova.data.hora.inico.obrigatoria}")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DH_INICIO")
    private Date dataHoraInicio;

    @NotNull(message = "{prova.data.hora.fim.obrigatoria}")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DH_FIM")
    private Date dataHoraFim;

    @OneToMany(mappedBy = "simuladoAluno", fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<SimuladoAlunoQuestao> questoesAluno;

    @Transient
    private Double nota;

    /**
     * Calcula a nota obtida pelo aluno no simulado.
     */
    private void calcularNota() {
        nota = 0.0;
        double respostasCertas = 0.0;

        if (questoesAluno != null && !questoesAluno.isEmpty()) {
            for (SimuladoAlunoQuestao provaAlunoQuestao : questoesAluno) {
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

    public Simulado getSimulado() {
        return simulado;
    }

    public void setSimulado(Simulado simulado) {
        this.simulado = simulado;
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

    public List<SimuladoAlunoQuestao> getQuestoesAluno() {
        return questoesAluno;
    }

    public void setQuestoesAluno(List<SimuladoAlunoQuestao> questoesAluno) {
        this.questoesAluno = questoesAluno;
    }

    public Double getNota() {
        calcularNota();
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

}
