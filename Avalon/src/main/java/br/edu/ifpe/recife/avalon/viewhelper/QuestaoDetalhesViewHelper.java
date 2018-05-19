/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.viewhelper;

import br.edu.ifpe.recife.avalon.model.questao.Questao;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author eduardo.f.amaral
 */
@Named(value = QuestaoDetalhesViewHelper.NOME)
@SessionScoped
public class QuestaoDetalhesViewHelper implements Serializable {
    
    public static final String NOME = "questaoDetalhesViewHelper";
    
    private Questao questaoDetalhes = new Questao();
    private boolean exibirModalDetalhes = false;
    
    public void inicializar(){
        fecharModalDetalhes();
    }
    
    /**
     * Método para exibir o modal de detalhes.
     * @param questao - Questão selecionada
     */
    public void exibirDetalhes(Questao questao){
        questaoDetalhes = questao;
        exibirModalDetalhes = true;
    }
    
    /**
     * Método para fechar o modal de detalhes.
     */
    public void fecharModalDetalhes(){
        questaoDetalhes = new Questao();
        exibirModalDetalhes = false;
    }

    public Questao getQuestaoDetalhes() {
        return questaoDetalhes;
    }
    
    public boolean isExibirModalDetalhes() {
        return exibirModalDetalhes;
    }
    
}
