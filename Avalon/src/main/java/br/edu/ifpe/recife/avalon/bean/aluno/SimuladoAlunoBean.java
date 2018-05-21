/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean.aluno;

import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.viewhelper.ComponenteCurricularViewHelper;
import br.edu.ifpe.recife.avalon.model.simulado.Simulado;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.servico.ComponenteCurricularServico;
import br.edu.ifpe.recife.avalon.servico.QuestaoServico;
import br.edu.ifpe.recife.avalon.servico.SimuladoServico;
import br.edu.ifpe.recife.avalon.util.AvalonUtil;
import br.edu.ifpe.recife.avalon.viewhelper.PesquisarSimuladoViewHelper;
import java.io.Serializable;
import java.util.ArrayList;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author eduardoamaral
 */
@Named(value = SimuladoAlunoBean.NOME)
@SessionScoped
public class SimuladoAlunoBean implements Serializable {
    
    public static final String NOME = "simuladoAlunoBean";
    private static final String GO_INICIAR_SIMULADO = "goIniciarSimulado";
    private static final String GO_RESULTADO_SIMULADO = "goResultadoSimulado";
    private static final String GO_PROCURAR_SIMULADO = "goProcurarSimulado";
    private static final String USUARIO = "usuario";
    
    @EJB
    private QuestaoServico questaoServico;
    
    @EJB
    private SimuladoServico simuladoServico;
    
    @EJB
    private ComponenteCurricularServico  componenteCurricularServico;
    
    private final ComponenteCurricularViewHelper componenteViewHelper;
    private final PesquisarSimuladoViewHelper pesquisarSimuladoViewHelper;
    private final HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private final Usuario usuarioLogado;
    
    private Simulado simuladoSelecionado;
    private List<Simulado> simulados;
    private List<Questao> questoesSimulado;
    
    public SimuladoAlunoBean() {
        usuarioLogado = (Usuario) sessao.getAttribute(USUARIO);
        componenteViewHelper = new ComponenteCurricularViewHelper();
        pesquisarSimuladoViewHelper = new PesquisarSimuladoViewHelper();
        simulados = new ArrayList<>();
    }
    
    /**
     * Método para iniciar a página de geração de prova.
     * @return rota para página de geração de prova
     */
    public String iniciarPagina(){
        componenteViewHelper.inicializar(componenteCurricularServico);
        pesquisarSimuladoViewHelper.inicializar(simuladoServico, usuarioLogado);
        limparTela();
        
        return GO_PROCURAR_SIMULADO;
    }
    
    /**
     * Método para iniciar um novo Simulado.
     * @param simulado
     * @return navegacao
     */
    public String iniciarSimulado(Simulado simulado){
        limparTelaSimulado();
        simuladoSelecionado = simulado;
        questoesSimulado = questaoServico.buscarQuestoesPorSimulado(simulado.getId());
        
        return GO_INICIAR_SIMULADO;
    }
    
    /**
     * Método para limpar os campos da tela listar simulados.
     */
    private void limparTela(){
        simuladoSelecionado = new Simulado();
    }
    
    /**
     * Método para limpar os campos da tela do simulado.
     */
    private void limparTelaSimulado() {
        
    }
    
    
    /**
     * Método para carregar as questões do usuário.
     */
    public void pesquisar() {
        simulados = pesquisarSimuladoViewHelper.pesquisar();
        if(simulados.isEmpty()){
            exibirMensagemPesquisaSemDados();
        }
    }
    
    /**
     * Método para exibição de mensagem "Pesquisa sem dados".
     */
    private void exibirMensagemPesquisaSemDados(){
        FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_INFO, AvalonUtil.getInstance().getMensagem("pesquisa.sem.dados"), null);
        FacesContext.getCurrentInstance().addMessage(null, mensagem);
    }
    
    /**
     * Método para exibir mensagem de erro.
     * @param mensagem - mensagem a ser exibida.
     */
    private void exibirMensagemError(String mensagem){
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, null);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }
    
    public ComponenteCurricularViewHelper getComponenteViewHelper() {
        return componenteViewHelper;
    }

    public PesquisarSimuladoViewHelper getPesquisarSimuladoViewHelper() {
        return pesquisarSimuladoViewHelper;
    }

    public List<Simulado> getSimulados() {
        return simulados;
    }

    public Simulado getSimuladoSelecionado() {
        return simuladoSelecionado;
    }

    public List<Questao> getQuestoesSimulado() {
        return questoesSimulado;
    }
    
}
