/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.questao;

import br.edu.ifpe.recife.avalon.util.AvalonUtil;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

/**
 *
 * @author aldenio
 */
@Entity
@DiscriminatorValue("M")
public class MultiplaEscolha extends Questao implements Serializable{
    
    @OneToMany(mappedBy = "questao", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Alternativa> alternativas;

    @Override
    public String formatarQuestao(){
        StringBuilder sb = new StringBuilder();
        
        for(int i = 0; i < this.alternativas.size(); i++){
            char opcao = (char) (i+97);
            sb.append(this.alternativas.get(i).formatarAlternativa(opcao));
            sb.append(AvalonUtil.quebrarLinha());
        }
        
        return sb.toString();
    }
    
    public List<Alternativa> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(List<Alternativa> alternativas) {
        this.alternativas = alternativas;
    }
    
}
