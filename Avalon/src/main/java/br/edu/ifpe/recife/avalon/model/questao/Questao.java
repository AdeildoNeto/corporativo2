/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.questao;

import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.util.AvalonUtil;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author eduardo.f.amaral
 */
@Entity
@Table(name = "TB_QUESTAO")
@NamedQueries(
        {
            @NamedQuery(
                    name = "Questao.PorId",
                    query = "SELECT q FROM Questao q WHERE q.id = :id AND q.ativa = true"
            )
            ,@NamedQuery(
                    name = "Questao.PorCriador",
                    query = "SELECT q FROM Questao q WHERE q.criador.id = :idCriador AND q.ativa = true"
            )
            ,@NamedQuery(
                    name = "Questao.PorTipo",
                    query = "SELECT q FROM Questao q WHERE q.tipo = :tipo AND q.ativa = true"
            )
            ,@NamedQuery(
                    name = "Questao.PorCriadorTipo",
                    query = "SELECT q FROM Questao q WHERE q.tipo = :tipo AND q.criador.id = :idCriador AND q.ativa = true"
            )
            ,@NamedQuery(
                    name = "Questao.PorEnunciadoTipo",
                    query = "SELECT q FROM Questao q WHERE q.tipo = :tipo AND q.enunciado = :enunciado AND q.ativa = true"
            )
        })
public class Questao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_QUESTAO")
    private Long id;

    @NotBlank(message = "{questao.enunciado.obrigatorio}")
    @Column(name = "TXT_ENUNCIADO")
    private String enunciado;

    @NotNull(message = "{questao.criador.obrigatorio}")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
    private Usuario criador;

    @NotNull(message = "{questao.tipo.obrigatorio}")
    @Enumerated(EnumType.STRING)
    @Column(name = "TXT_TIPO")
    private TipoQuestaoEnum tipo;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_CRIACAO")
    private Date dataCriacao;

    @Column(name = "SN_ATIVA", nullable = false)
    private Boolean ativa = true;

    @Transient
    private boolean selecionada;

    public String formatarQuestao(int numero) {
        StringBuilder sb = new StringBuilder();

        sb.append(numero + 1).append(")").append(" ");

        sb.append(this.enunciado);
        sb.append(AvalonUtil.quebrarLinha());

        if (TipoQuestaoEnum.VERDADEIRO_FALSO.equals(this.tipo)) {
            sb.append("( ) Verdadeiro");
            sb.append(AvalonUtil.quebrarLinha());
            sb.append("( ) Falso");
            sb.append(AvalonUtil.quebrarLinha());
            sb.append(AvalonUtil.quebrarLinha());
        }

        return sb.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public Usuario getCriador() {
        return criador;
    }

    public void setCriador(Usuario criador) {
        this.criador = criador;
    }

    public TipoQuestaoEnum getTipo() {
        return tipo;
    }

    public void setTipo(TipoQuestaoEnum tipo) {
        this.tipo = tipo;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public boolean isSelecionada() {
        return selecionada;
    }

    public void setSelecionada(boolean selecionada) {
        this.selecionada = selecionada;
    }

}
