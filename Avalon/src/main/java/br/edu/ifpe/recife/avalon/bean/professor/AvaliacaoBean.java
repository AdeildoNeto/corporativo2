/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean.professor;

import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.util.AvalonUtil;
import br.edu.ifpe.recife.avalon.viewhelper.PesquisarQuestaoViewHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author eduardoamaral
 */
public class AvaliacaoBean implements Serializable {
    
    private List<Questao> questoes;
    private Set<Questao> questoesSelecionadas;
    private boolean todosSelecionados;
    
    private final PesquisarQuestaoViewHelper pesquisarQuestoesViewHelper;

    public AvaliacaoBean() {
        questoes = new ArrayList<>();
        questoesSelecionadas = new HashSet<>();
        pesquisarQuestoesViewHelper = new PesquisarQuestaoViewHelper();
    }
    
    /**
     * Seleciona uma questão da lista de questões.
     *
     * @param questao - questão selecionada.
     */
    public void selecionarQuestao(Questao questao) {
        if (questao.isSelecionada()) {
            questoesSelecionadas.add(questao);
        } else {
            questoesSelecionadas.remove(questao);
            todosSelecionados = false;
        }
    }

    /**
     * Pesquisa as questões disponíveis para impressão.
     */
    public void pesquisar() {
        buscarQuestoes();
        if (questoes.isEmpty()) {
            exibirMensagem(FacesMessage.SEVERITY_INFO, AvalonUtil.getInstance().getMensagem("pesquisa.sem.dados"));
        }
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

    /**
     * Marca ou desmarca todas as questões disponíveis para impressão.
     */
    public void selecionarTodos() {
        questoesSelecionadas = new HashSet<>();

        for (Questao questao : questoes) {
            questao.setSelecionada(todosSelecionados);
            selecionarQuestao(questao);
        }

    }
    
    /**
     * Inicializa os componentes de questão.
     */
    protected void inicializarQuestoes() {
        todosSelecionados = false;
        questoesSelecionadas.clear();
        questoes.clear();
    }
    
    /**
     * Carrega as as questões do a partir do filtro informado.
     */
    private void buscarQuestoes() {
        this.questoes.clear();
        this.questoesSelecionadas.clear();
        this.todosSelecionados = false;
        this.questoes = pesquisarQuestoesViewHelper.pesquisar();
    }
    
    public List<Questao> getQuestoes() {
        return questoes;
    }

    public boolean isTodosSelecionados() {
        return todosSelecionados;
    }

    public void setTodosSelecionados(boolean todosSelecionados) {
        this.todosSelecionados = todosSelecionados;
    }

    public Set<Questao> getQuestoesSelecionadas() {
        return questoesSelecionadas;
    }

    public PesquisarQuestaoViewHelper getPesquisarQuestoesViewHelper() {
        return pesquisarQuestoesViewHelper;
    }
    
}
