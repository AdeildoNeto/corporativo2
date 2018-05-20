/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.questao;

import br.edu.ifpe.recife.avalon.util.AvalonUtil;
import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 
 * @author eduardoamaral
 */
@Entity
@Table(name = "TB_VERDADEIRO_FALSO")
@DiscriminatorValue("V")
@PrimaryKeyJoinColumn(name = "ID_VERDADEIRO_FALSO", referencedColumnName = "ID_QUESTAO")
public class VerdadeiroFalso extends Questao implements Serializable{
    
    @NotNull(message = "{questao.reposta.obrigatoria}")
    private boolean resposta;

    @Override
    public String formatarQuestao(){
        StringBuilder sb = new StringBuilder();

        sb.append("(  ) Verdadeiro");
        sb.append(AvalonUtil.quebrarLinha());
        sb.append("(  ) Falso");
        
        return sb.toString();
    }

    public boolean isResposta() {
        return resposta;
    }

    public void setResposta(boolean resposta) {
        this.resposta = resposta;
    }
    
}
