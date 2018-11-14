/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.questao;

import br.edu.ifpe.recife.avalon.model.questao.componente.ComponenteCurricular;
import br.edu.ifpe.recife.avalon.model.questao.enums.TipoQuestaoEnum;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.ManyToOne;
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
                    name = "Questao.PorProfessor",
                    query = "SELECT q FROM Questao q WHERE q.professor.email = :emailProfessor"
            )
            ,@NamedQuery(
                    name = "Questao.PorTipoValido",
                    query = "SELECT q FROM Questao q WHERE q.anulada = false"
                    + " AND q.tipo = :tipo"
                    + " AND q.enunciado = :enunciado"
                    + " AND q.professor.id = :idProfessor"
                    + " AND q.componenteCurricular.id = :idComponenteCurricular"
                    + " AND (:idQuestao IS NULL OR q.id <> :idQuestao)"
            )
            ,@NamedQuery(
                    name = "Questao.PorFiltroCompartilhada",
                    query = "SELECT q FROM Questao q WHERE q.anulada = false"
                    + " AND q.tipo = :tipo "
                    + " AND (q.professor.email = :emailUsuario OR (q.professor.email <> :emailUsuario AND q.compartilhada = true)) "
                    + " AND (:idComponenteCurricular is null OR :idComponenteCurricular = q.componenteCurricular.id) "
                    + " AND (:nomeProfessor is null OR (CONCAT(q.professor.nome, ' ', q.professor.sobrenome) like :nomeProfessor)) "
                    + " AND (:enunciado is null OR q.enunciado like :enunciado)"
                    + " AND (:questaoSimulado is null OR q.questaoSimulado = true)"
            )
            ,@NamedQuery(
                    name = "Questao.PorId",
                    query = "Select u from Questao u where u.id = :id")
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

    @NotNull(message = "{professor.obrigatorio}")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
    private Usuario professor;

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
    
    @Column(name = "SN_QUESTAO_SIMULADO", nullable = false)
    private boolean questaoSimulado = false;

    @Size(max = 300, message = "{questao.solucao.tamanho.maximo}")
    @Column(name = "TXT_SOLUCAO", columnDefinition = "varchar(300)")
    private String solucao;
    
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
    
    /**
     * Formata o enunciado da questão.
     * 
     * @param numeroQuestao
     * @return 
     */
    public String formatarEnunciado(int numeroQuestao){
        StringBuilder sb = new StringBuilder();
        sb.append(numeroQuestao);
        sb.append(") ");
        sb.append(enunciado);
        return sb.toString();
    }
    
    /**
     * Formata a resposta da questão.
     * 
     * @return 
     */
    public String formatarResposta(){
        return "";
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

    public Usuario getProfessor() {
        return professor;
    }

    public void setProfessor(Usuario professor) {
        this.professor = professor;
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

    public Imagem getImagem() {
        return imagem;
    }

    public void setImagem(Imagem imagem) {
        this.imagem = imagem;
    }

    public boolean isAnulada() {
        return anulada;
    }

    public boolean isQuestaoSimulado() {
        return questaoSimulado;
    }

    public void setQuestaoSimulado(boolean questaoSimulado) {
        this.questaoSimulado = questaoSimulado;
    }
    
    public void setAnulada(boolean anulada) {
        this.anulada = anulada;
    }

    public String getSolucao() {
        return solucao;
    }

    public void setSolucao(String solucao) {
        this.solucao = solucao;
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
        return Objects.equals(this.id, other.id);
    }

}
