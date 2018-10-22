/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.avaliacao.simulado;

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
@Table(name = "TB_QUESTAO_SIMULADO")
@DiscriminatorValue("S")
@PrimaryKeyJoinColumn(name = "ID_QUESTAO_SIMULADO", referencedColumnName = "ID_QUESTAO_AVALIACAO")
public class QuestaoSimulado extends QuestaoAvaliacao {
    
    @NotNull(message = "{simulado.obrigatorio}")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_AVALIACAO", referencedColumnName = "ID_AVALIACAO")
    private Simulado simulado;

    public Simulado getSimulado() {
        return simulado;
    }

    public void setSimulado(Simulado simulado) {
        this.simulado = simulado;
    }

}
