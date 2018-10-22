/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.avaliacao;

import br.edu.ifpe.recife.avalon.model.questao.componente.ComponenteCurricular;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import java.io.Serializable;
import java.util.Date;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author eduardoamaral
 */
@MappedSuperclass
public class Avaliacao implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_AVALIACAO")
    private Long id;

    @NotBlank(message = "{prova.titulo.obrigatorio}")
    @Size(max = 40, message = "{titulo.tamanho.maximo}")
    @Column(name = "TXT_TITULO")
    private String titulo;

    @NotNull(message = "{professor.obrigatorio}")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
    private Usuario professor;

    @NotNull(message = "{componente.curricular.obrigatorio}")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_COMPONENTE_CURRICULAR", referencedColumnName = "ID")
    private ComponenteCurricular componenteCurricular;

    @NotNull(message = "{prova.data.criacao.obrigatoria}")
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_CRIACAO")
    private Date dataCriacao;
    
    @Column(name = "SN_ATIVA", nullable = false)
    private boolean ativa = true;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Usuario getProfessor() {
        return professor;
    }

    public void setProfessor(Usuario professor) {
        this.professor = professor;
    }

    public ComponenteCurricular getComponenteCurricular() {
        return componenteCurricular;
    }

    public void setComponenteCurricular(ComponenteCurricular componenteCurricular) {
        this.componenteCurricular = componenteCurricular;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    
    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }
    
}
