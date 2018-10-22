/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.avaliacao.prova;

import br.edu.ifpe.recife.avalon.model.avaliacao.QuestaoAvaliacao;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author eduardoamaral
 */
@Entity
@Table(name = "TB_QUESTAO_PROVA")
@DiscriminatorValue("P")
@PrimaryKeyJoinColumn(name = "ID_QUESTAO_PROVA", referencedColumnName = "ID_QUESTAO_AVALIACAO")
public class QuestaoProva extends QuestaoAvaliacao {

    @NotNull(message = "{prova.obrigatoria}")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_AVALIACAO", referencedColumnName = "ID_AVALIACAO")
    private Prova prova;

    public Prova getProva() {
        return prova;
    }

    public void setProva(Prova prova) {
        this.prova = prova;
    }

}
