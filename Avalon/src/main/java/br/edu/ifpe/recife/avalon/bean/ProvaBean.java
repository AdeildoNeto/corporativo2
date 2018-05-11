/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean;

import br.edu.ifpe.recife.avalon.model.questao.ComponenteCurricular;
import br.edu.ifpe.recife.avalon.model.questao.FiltroQuestao;
import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.TipoQuestaoEnum;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.servico.ComponenteCurricularServico;
import br.edu.ifpe.recife.avalon.servico.QuestaoServico;
import br.edu.ifpe.recife.avalon.util.AvalonUtil;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
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
    private ComponenteCurricularServico componenteServico;

    private Usuario usuarioLogado;
    
    private List<TipoQuestaoEnum> tipoQuestoes = new ArrayList<>();

    private List<Questao> questoes = new ArrayList<>();

    private Set<Questao> questoesSelecionadas = new HashSet<>();
    
    private Questao questaoDetalhes = new Questao();
    
    private boolean todosSelecionados = false;
    
    private boolean exibirModalDetalhes = false;
    
    private HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    
    private FiltroQuestao filtro = new FiltroQuestao();
    
    private List<ComponenteCurricular> todosComponentesCurricular = new ArrayList<>();
    
    /**
     * Método para iniciar a página de geração de prova.
     * @return rota para página de geração de prova
     */
    public String iniciarPagina(){
        usuarioLogado = (Usuario) sessao.getAttribute("usuario");
        limparTela();
        carregarTiposQuestao();
        carregarTodosComponentesCurricular();
        buscarQuestoes();
        return "goGerarProva";
    }
    
    public ProvaBean() {
        usuarioLogado = (Usuario) sessao.getAttribute("usuario");
    }
    
    /**
     * Método para carregar as questões do usuário.
     */
    private void buscarQuestoes() {
        this.questoesSelecionadas.clear();
        this.todosSelecionados = false;
        this.filtro.setEmailUsuario(usuarioLogado.getEmail());
        this.questoes = questaoServico.buscarQuestoesPorFiltro(filtro);
    }

    /**
     * Método responsável por carregar os tipos de questão disponíveis.
     */
    private void carregarTiposQuestao() {
        this.tipoQuestoes = Arrays.asList(TipoQuestaoEnum.values());        
    }
    
    private void carregarTodosComponentesCurricular(){
        this.todosComponentesCurricular = componenteServico.buscarTodosComponentes();
    }

    /**
     * Método para limpar os campos da tela.
     */
    private void limparTela() {
        Long idComponenteSelecionado = getTodosComponentesCurricular().isEmpty() ? null : getTodosComponentesCurricular().get(0).getId();
        todosSelecionados = false;
        exibirModalDetalhes = false;
        questoesSelecionadas.clear();
        this.filtro = new FiltroQuestao();
        this.filtro.setIdComponenteCurricular(idComponenteSelecionado);
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
    public void pesquisar(){
        buscarQuestoes();
        if(questoes.isEmpty()){
            exibirMensagemPesquisaSemDados();
        }
    }
    
    private void exibirMensagemPesquisaSemDados(){
        FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_INFO, AvalonUtil.getInstance().getMensagem("pesquisa.sem.dados"), null);
        FacesContext.getCurrentInstance().addMessage(null, mensagem);
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
    
    /**
     * Método para exibir o modal de detalhes
     * @param questao 
     */
    public void exibirDetalhes(Questao questao){
        questaoDetalhes = questao;
        exibirModalDetalhes = true;
    }
    
    /**
     * Método para fechar o modal de detalhes
     */
    public void fecharModalDetalhes(){
        questaoDetalhes = new Questao();
        exibirModalDetalhes = false;
    }
    
    /**
     * Método para buscar todos os componentes curricular cadastrados.
     * @return lista de componentes curricular
     */
    public List<ComponenteCurricular> getTodosComponentesCurricular(){
        return todosComponentesCurricular;
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

    public Questao getQuestaoDetalhes() {
        return questaoDetalhes;
    }

    public void setQuestaoDetalhes(Questao questaoDetalhes) {
        this.questaoDetalhes = questaoDetalhes;
    }

    public boolean isExibirModalDetalhes() {
        return exibirModalDetalhes;
    }

    public void setExibirModalDetalhes(boolean exibirModalDetalhes) {
        this.exibirModalDetalhes = exibirModalDetalhes;
    }

    public FiltroQuestao getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroQuestao filtro) {
        this.filtro = filtro;
    }
    
}
