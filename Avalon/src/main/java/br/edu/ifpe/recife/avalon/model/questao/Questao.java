/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.questao;

import br.edu.ifpe.recife.avalon.model.simulado.Simulado;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.util.AvalonUtil;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author eduardo.f.amaral
 */
@Entity
@Table(name = "TB_QUESTAO")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("Q")
@Access(AccessType.FIELD)
@NamedQueries(
        {
            @NamedQuery(
                    name = "Questao.PorCriador",
                    query = "SELECT q FROM Questao q WHERE q.criador.email = :emailCriador AND "
                    + "q.ativa = true"
            )
            ,@NamedQuery(
                    name = "Questao.PorTipo",
                    query = "SELECT q FROM Questao q WHERE q.tipo = :tipo AND "
                    + "q.ativa = true"
            )
            ,@NamedQuery(
                    name = "Questao.PorCriadorTipo",
                    query = "SELECT q FROM Questao q WHERE q.tipo = :tipo AND "
                    + "q.criador.email = :emailCriador AND "
                    + "q.ativa = true"
            )
            ,@NamedQuery(
                    name = "Questao.PorEnunciadoTipo",
                    query = "SELECT q FROM Questao q WHERE q.tipo = :tipo AND "
                    + "q.enunciado = :enunciado AND "
                    + "q.ativa = true"
            )
            ,@NamedQuery(
                    name = "Questao.PorEnunciadoTipoId",
                    query = "SELECT q FROM Questao q WHERE q.tipo = :tipo AND "
                    + "q.enunciado = :enunciado AND "
                    + "q.id <> :id AND q.ativa = true"
            )
            ,@NamedQuery(
                    name = "Questao.PorTipoValido",
                    query = "SELECT q FROM Questao q WHERE q.tipo = :tipo AND "
                    + "q.enunciado = :enunciado AND "
                    + "q.criador.id = :idCriador AND "
                    + "q.componenteCurricular.id = :idComponenteCurricular AND "
                    + "q.ativa = true"
            )
            ,@NamedQuery(
                    name = "Questao.PorFiltroCompartilhada",
                    query = "SELECT q FROM Questao q WHERE q.ativa = true AND q.tipo = :tipo "
                    + "AND (q.criador.email = :emailUsuario OR (q.criador.email <> :emailUsuario AND q.compartilhada = true)) "
                    + "AND (:idComponenteCurricular is null OR :idComponenteCurricular = q.componenteCurricular.id) "
                    + "AND (:nomeCriador is null OR (CONCAT(q.criador.nome, ' ', q.criador.sobrenome) like :nomeCriador)) "
                    + "AND (:enunciado is null OR q.enunciado like :enunciado)"
            )
        })
public class Questao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_QUESTAO")
    private Long id;

    @NotBlank(message = "{questao.enunciado.obrigatorio}")
    @Size(max = 2000, message = "{questao.tamanho.enunciado}")
    @Column(name = "TXT_ENUNCIADO", columnDefinition = "varchar(2000)")
    private String enunciado;

    @NotNull(message = "{criador.obrigatorio}")
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

    @Column(name = "SN_COMPARTILHADA", nullable = false)
    private Boolean compartilhada = true;

    @NotNull(message = "{componente.curricular.obrigatorio}")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_COMPONENTE_CURRICULAR", referencedColumnName = "ID")
    private ComponenteCurricular componenteCurricular;
    
    @ManyToMany(mappedBy = "questoes")
    private List<Simulado> simulados; 

    @Transient
    private boolean selecionada;

    /**
     * Método para formatar apresentação da questão de acordo com o tipo.
     *
     * @return questao formatada.
     */
    public String formatarQuestao() {
        StringBuilder sb = new StringBuilder();

        if (TipoQuestaoEnum.VERDADEIRO_FALSO.equals(this.tipo)) {
            sb.append("(  ) Verdadeiro");
            sb.append(AvalonUtil.quebrarLinha());
            sb.append("(  ) Falso");
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

    public Boolean getCompartilhada() {
        return compartilhada;
    }

    public void setCompartilhada(Boolean compartilhada) {
        this.compartilhada = compartilhada;
    }

    public ComponenteCurricular getComponenteCurricular() {
        return componenteCurricular;
    }

    public void setComponenteCurricular(ComponenteCurricular componenteCurricular) {
        this.componenteCurricular = componenteCurricular;
    }

    public List<Simulado> getSimulados() {
        return simulados;
    }

    public void setSimulados(List<Simulado> simulados) {
        this.simulados = simulados;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Questao other = (Questao) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
