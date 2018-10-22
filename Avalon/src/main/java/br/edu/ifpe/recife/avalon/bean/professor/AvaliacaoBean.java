/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean.professor;

import br.edu.ifpe.recife.avalon.viewhelper.PesquisarQuestaoViewHelper;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author eduardoamaral
 */
public class AvaliacaoBean implements Serializable {
    
    private final PesquisarQuestaoViewHelper pesquisarQuestoesViewHelper;
    private boolean todosSelecionados;

    public AvaliacaoBean() {
        pesquisarQuestoesViewHelper = new PesquisarQuestaoViewHelper();
        todosSelecionados = false;
    }
    
    /**
     * Exibe mensagem na tela.
     * @param severity
     * @param mensagem
     */
    protected void exibirMensagem(FacesMessage.Severity severity, String mensagem) {
        FacesMessage facesMessage = new FacesMessage(severity, mensagem, null);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }

    public boolean isTodosSelecionados() {
        return todosSelecionados;
    }

    public void setTodosSelecionados(boolean todosSelecionados) {
        this.todosSelecionados = todosSelecionados;
    }

    public PesquisarQuestaoViewHelper getPesquisarQuestoesViewHelper() {
        return pesquisarQuestoesViewHelper;
    }
    
}
