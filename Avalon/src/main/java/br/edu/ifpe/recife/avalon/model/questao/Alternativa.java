/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.questao;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author aldenio
 */
@Entity
@Table(name = "TB_ALTERNATIVAS")
public class Alternativa implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ALTERNATIVA")
    private Long id;

    @NotBlank(message = "{questao.alternativa.obrigatorio}")
    @Size(max = 255, message = "{alternativa.tamanho.maximo}")
    @Column(name = "TXT_DESCRICAO")
    private String descricao;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_QUESTAO", referencedColumnName = "ID_QUESTAO")
    private MultiplaEscolha questao;

    public String formatarAlternativa(char opcao){
        StringBuilder sb = new StringBuilder();
        
        sb.append(opcao).append(")").append(" ");
        sb.append(this.descricao);
        
        return sb.toString();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public MultiplaEscolha getQuestao() {
        return questao;
    }

    public void setQuestao(MultiplaEscolha questao) {
        this.questao = questao;
    }

}
