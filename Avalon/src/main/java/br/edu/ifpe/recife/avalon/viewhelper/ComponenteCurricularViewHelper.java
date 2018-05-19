/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.viewhelper;

import br.edu.ifpe.recife.avalon.model.questao.ComponenteCurricular;
import br.edu.ifpe.recife.avalon.servico.ComponenteCurricularServico;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author eduardo.f.amaral
 */
public class ComponenteCurricularViewHelper implements Serializable {
    
    private ComponenteCurricularServico componenteServico;

    private List<ComponenteCurricular> todosComponentesCurricular = new ArrayList<>();
    
    public void inicializar(ComponenteCurricularServico servico){
        componenteServico = servico;
        carregarTodosComponentesCurricular();
    }
    
    /**
     * Método para buscar todos os componentes curricular cadastrados.
     */
    private void carregarTodosComponentesCurricular(){
        this.todosComponentesCurricular = componenteServico.buscarTodosComponentes();
    }

    /*
        GETTERS AND SETTERS
    */
    
    public List<ComponenteCurricular> getTodosComponentesCurricular(){
        return todosComponentesCurricular;
    }
    
}
