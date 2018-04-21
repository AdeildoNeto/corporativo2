/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean;

import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.TipoQuestaoEnum;
import br.edu.ifpe.recife.avalon.servico.QuestaoServico;
import br.edu.ifpe.recife.avalon.servico.UsuarioServico;
import br.edu.ifpe.recife.avalon.util.AvalonUtil;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author eduardo.f.amaral
 */
@Named(value = "provaBean")
@SessionScoped
public class ProvaBean implements Serializable {

    @EJB
    private QuestaoServico questaoServico;

    @EJB
    private UsuarioServico usuarioServico;

    private List<TipoQuestaoEnum> tipoQuestoes = new ArrayList<>();

    private TipoQuestaoEnum tipoSelecionado = TipoQuestaoEnum.DISCURSIVA;

    private List<Questao> questoes = new ArrayList<>();

    private Set<Questao> questoesSelecionadas = new HashSet<>();
    
    private boolean todosSelecionados = false;
    
    /**
     * Método para iniciar a página de geração de prova.
     * @return rota para página de geração de prova
     */
    public String iniciarPagina(){
        limparTela();
        buscarQuestoes();
        return "goGerarProva";
    }
    
    public ProvaBean() {
        this.limparTela();
    }
    
    /**
     * Método para carregar as questões do usuário.
     */
    private void buscarQuestoes() {
        this.questoes = questaoServico.buscarQuestoesPorCriadorTipo("email2@email.com", tipoSelecionado);
    }

    /**
     * Método responsável por carregar os tipos de questão disponíveis.
     */
    private void carregarTiposQuestao() {
        this.tipoQuestoes.add(TipoQuestaoEnum.DISCURSIVA);
        this.tipoQuestoes.add(TipoQuestaoEnum.MULTIPLA_ESCOLHA);
        this.tipoQuestoes.add(TipoQuestaoEnum.VERDADEIRO_FALSO);
    }

    /**
     * Método para limpar os campos da tela.
     */
    private void limparTela() {
        tipoSelecionado = TipoQuestaoEnum.DISCURSIVA;
        todosSelecionados = false;
        questoesSelecionadas = new HashSet<>();
    }
    
    /**
     * Método para selecionar uma questão da lista de questões.
     * @param questao 
     */
    public void selecionarQuestao(Questao questao){
        if(questao.isSelecionada()){
            questoesSelecionadas.add(questao);
        }else{
            questoesSelecionadas.remove(questao);
            todosSelecionados = false;
        }
    }
    
    /**
     * Método para atualizar a lista de questões disponíveis para impressão.
     */
    public void selecionarTipoQuestao(){
        buscarQuestoes();
    }
    
    /**
     * Método para selecionar todas as questões disponíveis.
     */
    public void selecionarTodos(){
        questoesSelecionadas = new HashSet<>();
        
        for(Questao questao : questoes){
            questao.setSelecionada(todosSelecionados);
            selecionarQuestao(questao);
        }
        
    }
    
    /**
     * Método para gerar uma prova a partir das questões selecionada.
     */
    public void imprimirProva(){
        if(questoesSelecionadas.isEmpty()){
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_ERROR, AvalonUtil.getInstance().getMensagemValidacao("prova.selecionar.questoes"), null);
            FacesContext.getCurrentInstance().addMessage(null, mensagem);
        }else{
            RequestContext.getCurrentInstance().execute("window.open('http://localhost:8080/Avalon/professor/prova/impressao.xhtml')");
        }
    }
    
    /*
        GETTERS AND SETTERS
    */
    
    public List<TipoQuestaoEnum> getTipoQuestoes() {
        return tipoQuestoes;
    }

    public void setTipoQuestoes(List<TipoQuestaoEnum> tipoQuestoes) {
        this.tipoQuestoes = tipoQuestoes;
    }

    public TipoQuestaoEnum getTipoSelecionado() {
        return tipoSelecionado;
    }

    public void setTipoSelecionado(TipoQuestaoEnum tipoSelecionado) {
        this.tipoSelecionado = tipoSelecionado;
    }

    public List<Questao> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(List<Questao> questoes) {
        this.questoes = questoes;
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

    public void setQuestoesSelecionadas(Set<Questao> questoesSelecionadas) {
        this.questoesSelecionadas = questoesSelecionadas;
    }
    
}
