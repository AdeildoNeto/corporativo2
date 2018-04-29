/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.helper;

import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.questao.ComponenteCurricular;
import br.edu.ifpe.recife.avalon.servico.ComponenteCurricularServico;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.validation.Valid;

/**
 *
 * @author eduardoamaral
 */
@ManagedBean(name = "componenteCurricularHelper")
@SessionScoped
public class ComponenteCurricularHelper implements Serializable{
    
    @EJB
    private ComponenteCurricularServico servico;
    
    @Valid
    private ComponenteCurricular novoComponenteCurricular = new ComponenteCurricular();
    
    private ComponenteCurricular componenteSelecionado = null;
    
    private boolean exibirModalComponente = false;

    public ComponenteCurricularHelper() {
    }
    
    public void init(){
        novoComponenteCurricular = new ComponenteCurricular();
        exibirModalComponente = false;
    }
    
    public void adicionarNovoComponente(){
        if(novoComponenteCurricular.equals(componenteSelecionado)){
            novoComponenteCurricular = new ComponenteCurricular();
            exibirModalComponente = true;
        }
    }
    
    public void salvarComponenteCurricular(){
        try {
            servico.salvar(novoComponenteCurricular);
        } catch (ValidacaoException ex) {
            exibirModalComponente = false;
            exibirMensagem(ex.getMessage());
        }
    }
    
    /**
     * Método para exibição de mensagens.
     */
    private void exibirMensagem(String mensagem) {
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, null);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }
    
    public List<ComponenteCurricular> getTodosComponentesCurricular(){
        return servico.buscarTodosComponentes();
    }

    public ComponenteCurricular getNovoComponenteCurricular() {
        return novoComponenteCurricular;
    }

    public void setNovoComponenteCurricular(ComponenteCurricular novoComponenteCurricular) {
        this.novoComponenteCurricular = novoComponenteCurricular;
    }

    public boolean isExibirModalComponente() {
        return exibirModalComponente;
    }

    public void setExibirModalComponente(boolean exibirModalComponente) {
        this.exibirModalComponente = exibirModalComponente;
    }
    
}
