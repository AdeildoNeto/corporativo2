/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean.professor;

import br.edu.ifpe.recife.avalon.viewhelper.ComponenteCurricularViewHelper;
import br.edu.ifpe.recife.avalon.viewhelper.PesquisarQuestaoViewHelper;
import br.edu.ifpe.recife.avalon.viewhelper.QuestaoDetalhesViewHelper;
import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.simulado.Simulado;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.servico.ComponenteCurricularServico;
import br.edu.ifpe.recife.avalon.servico.QuestaoServico;
import br.edu.ifpe.recife.avalon.servico.SimuladoServico;
import br.edu.ifpe.recife.avalon.util.AvalonUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author eduardoamaral
 */
@Named(value = SimuladoBean.NOME)
@SessionScoped
public class SimuladoBean implements Serializable {

    public static final String NOME = "simuladoBean";
    private static final String GO_GERAR_SIMULADO = "goGerarSimulado";
    private static final String GO_LISTAR_SIMULADO = "goLisarSimulado";
    private static final String USUARIO = "usuario";

    @EJB
    private QuestaoServico questaoServico;

    @EJB
    private SimuladoServico simuladoServico;

    @EJB
    private ComponenteCurricularServico componenteCurricularServico;

    private final ComponenteCurricularViewHelper componenteViewHelper;
    private final QuestaoDetalhesViewHelper detalhesViewHelper;
    private final PesquisarQuestaoViewHelper pesquisarViewHelper;
    private final HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private final Usuario usuarioLogado;

    private Simulado novoSimulado;
    private Simulado simuladoSelecionado;
    private List<Questao> questoes;
    private List<Simulado> simulados;
    private Set<Questao> questoesSelecionadas;
    private boolean todosSelecionados;
    private boolean exibirModalTitulo;

    public SimuladoBean() {
        usuarioLogado = (Usuario) sessao.getAttribute(USUARIO);
        componenteViewHelper = new ComponenteCurricularViewHelper();
        detalhesViewHelper = new QuestaoDetalhesViewHelper();
        pesquisarViewHelper = new PesquisarQuestaoViewHelper();
        questoes = new ArrayList<>();
        questoesSelecionadas = new HashSet<>();
        novoSimulado = new Simulado();
    }

    /**
     * Método para iniciar a página de geração de prova.
     *
     * @return rota para página de geração de prova
     */
    public String iniciarPagina() {
        limparTela();
        buscarSimulados();

        return GO_LISTAR_SIMULADO;
    }

    public String iniciarPaginaGerarSimulado() {
        componenteViewHelper.inicializar(componenteCurricularServico);
        detalhesViewHelper.inicializar();
        pesquisarViewHelper.inicializar(questaoServico, usuarioLogado, true);
        limparTelaGerarSimulado();

        return GO_GERAR_SIMULADO;
    }

    /**
     * Método para limpar os campos da tela listar simulados.
     */
    private void limparTela() {
        novoSimulado = new Simulado();
        simuladoSelecionado = new Simulado();
        exibirModalTitulo = false;
    }

    /**
     * Método para limpar os campos da tela gerar simulado.
     */
    private void limparTelaGerarSimulado() {
        todosSelecionados = false;
        questoesSelecionadas.clear();
        questoes.clear();
    }

    /**
     * Método para carregar as questões do usuário.
     */
    private void buscarQuestoes() {
        this.questoes.clear();
        this.questoesSelecionadas.clear();
        this.todosSelecionados = false;
        this.questoes = pesquisarViewHelper.pesquisar();
    }

    /**
     * Método para carregar as questões do usuário.
     */
    private void buscarSimulados() {
        simulados = simuladoServico.buscarSimuladosPorCriador(usuarioLogado.getEmail());
    }

    /**
     * Método para selecionar uma questão da lista de questões.
     *
     * @param questao
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
     * Método para atualizar a lista de questões disponíveis para impressão.
     */
    public void pesquisar() {
        buscarQuestoes();
        if (questoes.isEmpty()) {
            exibirMensagemPesquisaSemDados();
        }
    }

    /**
     * Método para exibição de mensagem "Pesquisa sem dados".
     */
    private void exibirMensagemPesquisaSemDados() {
        FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_INFO, AvalonUtil.getInstance().getMensagem("pesquisa.sem.dados"), null);
        FacesContext.getCurrentInstance().addMessage(null, mensagem);
    }

    /**
     * Método para selecionar todas as questões disponíveis.
     */
    public void selecionarTodos() {
        questoesSelecionadas = new HashSet<>();

        for (Questao questao : questoes) {
            questao.setSelecionada(todosSelecionados);
            selecionarQuestao(questao);
        }

    }

    /**
     * Método para gerar uma prova a partir das questões selecionada.
     *
     * @return navegacao
     */
    public String salvar() {
        String navegacao = null;

        try {
            novoSimulado.setComponenteCurricular(componenteViewHelper.getComponenteCurricularPorId(pesquisarViewHelper.getFiltro().getIdComponenteCurricular()));
            novoSimulado.setDataCriacao(Calendar.getInstance().getTime());
            novoSimulado.setCriador(usuarioLogado);
            novoSimulado.setQuestoes(new ArrayList<Questao>());
            novoSimulado.getQuestoes().addAll(questoesSelecionadas);
            simuladoServico.salvar(novoSimulado);
            navegacao = iniciarPagina();
        } catch (ValidacaoException ex) {
            exibirMensagemError(ex.getMessage());
        }

        return navegacao;
    }

    /**
     * Método para exibir mensagem de erro.
     *
     * @param mensagem - mensagem a ser exibida.
     */
    private void exibirMensagemError(String mensagem) {
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, null);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }

    /**
     * Método para selecionar um simulado.
     *
     * @param simulado
     */
    public void selecionarSimulado(Simulado simulado) {
        simuladoSelecionado = simulado;
    }

    /**
     * Método para excluir um simulado.
     */
    public void excluir() {
        simuladoServico.remover(simuladoSelecionado);
        simulados.remove(simuladoSelecionado);
        simuladoSelecionado = null;
    }

    public void exibirModalTitulo() {
        novoSimulado = new Simulado();
        exibirModalTitulo = true;
    }

    public void fecharModalTitulo() {
        exibirModalTitulo = false;
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

    public Simulado getNovoSimulado() {
        return novoSimulado;
    }

    public ComponenteCurricularViewHelper getComponenteViewHelper() {
        return componenteViewHelper;
    }

    public QuestaoDetalhesViewHelper getDetalhesViewHelper() {
        return detalhesViewHelper;
    }

    public PesquisarQuestaoViewHelper getPesquisarViewHelper() {
        return pesquisarViewHelper;
    }

    public List<Simulado> getSimulados() {
        return simulados;
    }

    public boolean isExibirModalTitulo() {
        return exibirModalTitulo;
    }

}
