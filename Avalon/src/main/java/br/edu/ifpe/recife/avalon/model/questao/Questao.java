/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.questao;

import br.edu.ifpe.recife.avalon.model.prova.Prova;
import br.edu.ifpe.recife.avalon.model.simulado.Simulado;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
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
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author eduardoamaral
 */
@Entity
@Table(name = "TB_QUESTAO")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorValue("Q")
@Access(AccessType.FIELD)
@NamedQueries(
        {
            @NamedQuery(
                    name = "Questao.PorCriador",
                    query = "SELECT q FROM Questao q WHERE q.criador.email = :emailCriador"
            )
            ,@NamedQuery(
                    name = "Questao.PorTipoValido",
                    query = "SELECT q FROM Questao q WHERE q.anulada = false"
                    + " AND q.tipo = :tipo"
                    + " AND q.enunciado = :enunciado"
                    + " AND q.criador.id = :idCriador"
                    + " AND q.componenteCurricular.id = :idComponenteCurricular"
                    + " AND (:idQuestao IS NULL OR q.id <> :idQuestao)"
            )
            ,@NamedQuery(
                    name = "Questao.PorFiltroCompartilhada",
                    query = "SELECT q FROM Questao q WHERE q.anulada = false"
                    + " AND q.tipo = :tipo "
                    + " AND (q.criador.email = :emailUsuario OR (q.criador.email <> :emailUsuario AND q.compartilhada = true)) "
                    + " AND (:idComponenteCurricular is null OR :idComponenteCurricular = q.componenteCurricular.id) "
                    + " AND (:nomeCriador is null OR (CONCAT(q.criador.nome, ' ', q.criador.sobrenome) like :nomeCriador)) "
                    + " AND (:enunciado is null OR q.enunciado like :enunciado)"
            )
            ,@NamedQuery(
                    name = "Questao.PorId",
                    query = "Select u from Questao u where u.id = :id")
        })
@NamedNativeQueries(
        {
            @NamedNativeQuery(
                    name = "Questao.PorSimulado",
                    query = "SELECT Q.*, VF.*, MS.* FROM TB_QUESTAO Q LEFT OUTER JOIN TB_VERDADEIRO_FALSO VF "
                    + "ON (Q.ID_QUESTAO = VF.ID_VERDADEIRO_FALSO) "
                    + "LEFT OUTER JOIN TB_MULTIPLA_ESCOLHA MS "
                    + "ON (Q.ID_QUESTAO = MS.ID_MULTIPLA_ESCOLHA), "
                    + "TB_QUESTOES_SIMULADO QS "
                    + "WHERE QS.ID_SIMULADO = ?"
                    + " AND QS.ID_QUESTAO = Q.ID_QUESTAO"
                    + " AND (Q.ID_QUESTAO = VF.ID_VERDADEIRO_FALSO OR Q.ID_QUESTAO = MS.ID_MULTIPLA_ESCOLHA)",
                    resultClass = Questao.class
            )
            ,
            @NamedNativeQuery(
                    name = "Questao.PorProva",
                    query = "SELECT Q.*, VF.*, MS.* FROM TB_QUESTAO Q LEFT OUTER JOIN TB_VERDADEIRO_FALSO VF "
                    + "ON (Q.ID_QUESTAO = VF.ID_VERDADEIRO_FALSO) "
                    + "LEFT OUTER JOIN TB_MULTIPLA_ESCOLHA MS "
                    + "ON (Q.ID_QUESTAO = MS.ID_MULTIPLA_ESCOLHA), "
                    + "TB_QUESTOES_PROVA QP "
                    + "WHERE QP.ID_PROVA = ?"
                    + " AND QP.ID_QUESTAO = Q.ID_QUESTAO"
                    + " AND (Q.ID_QUESTAO = VF.ID_VERDADEIRO_FALSO OR Q.ID_QUESTAO = MS.ID_MULTIPLA_ESCOLHA)",
                    resultClass = Questao.class
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

    @Column(name = "SN_COMPARTILHADA", nullable = false)
    private Boolean compartilhada = true;

    @Column(name = "SN_ANULADA", nullable = false)
    private boolean anulada = false;

    @NotNull(message = "{componente.curricular.obrigatorio}")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_COMPONENTE_CURRICULAR", referencedColumnName = "ID")
    private ComponenteCurricular componenteCurricular;

    @ManyToMany(mappedBy = "questoes")
    private List<Simulado> simulados;

    @ManyToMany(mappedBy = "questoes")
    private List<Prova> provas;

    @Transient
    private boolean selecionada;

    @Valid
    @Embedded
    private Imagem imagem;

    /**
     * Formata a apresentação da questão de acordo com seu tipo.
     *
     * @return questao formatada.
     */
    public String formatarQuestao() {
        StringBuilder sb = new StringBuilder();
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

    public List<Prova> getProvas() {
        return provas;
    }

    public void setProvas(List<Prova> provas) {
        this.provas = provas;
    }

    public Imagem getImagem() {
        return imagem;
    }

    public void setImagem(Imagem imagem) {
        this.imagem = imagem;
    }

    public boolean isAnulada() {
        return anulada;
    }

    public void setAnulada(boolean anulada) {
        this.anulada = anulada;
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
