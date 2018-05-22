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
import br.edu.ifpe.recife.avalon.viewhelper.VisualizarSimuladoViewHelper;
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
    private static final String GO_VISUALIZAR_SIMULADO = "goVisualizarSimulado";
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
    private final VisualizarSimuladoViewHelper visualizarViewHelper;
    private final HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private final Usuario usuarioLogado;

    private Simulado novoSimulado;
    private Simulado simuladoSelecionado;
    private List<Questao> questoes;
    private List<Simulado> simulados;
    private Set<Questao> questoesSelecionadas;
    private boolean todosSelecionados;
    private boolean exibirModalTitulo;

    /**
     * Cria uma nova instância de <code>SimuladoBean</code>.
     */
    public SimuladoBean() {
        usuarioLogado = (Usuario) sessao.getAttribute(USUARIO);
        componenteViewHelper = new ComponenteCurricularViewHelper();
        detalhesViewHelper = new QuestaoDetalhesViewHelper();
        pesquisarViewHelper = new PesquisarQuestaoViewHelper();
        visualizarViewHelper = new VisualizarSimuladoViewHelper();
        questoes = new ArrayList<>();
        questoesSelecionadas = new HashSet<>();
        novoSimulado = new Simulado();
    }

    /**
     * Inicializa os dados necessários para a página de geração de prova.
     *
     * @return rota para página de geração de prova
     */
    public String iniciarPagina() {
        limparTela();
        buscarSimulados();

        return GO_LISTAR_SIMULADO;
    }

    /**
     * Inicializa os dados necessários para p[agina gerar novo simulado.
     * 
     * @return navegação.
     */
    public String iniciarPaginaGerarSimulado() {
        componenteViewHelper.inicializar(componenteCurricularServico);
        detalhesViewHelper.inicializar();
        pesquisarViewHelper.inicializar(questaoServico, usuarioLogado, true);
        limparTelaGerarSimulado();

        return GO_GERAR_SIMULADO;
    }

    /**
     * Inicializa os dados necessários para a página visualizar simulado.
     * 
     * @param simulado - simulado selecionado.
     * @return navegacao
     */
    public String iniciarPaginaVisualizarSimulado(Simulado simulado) {
        visualizarViewHelper.inicializar(questaoServico);
        simuladoSelecionado = simulado;

        if (!simuladoSelecionado.getQuestoes().isEmpty()) {
            try {
                visualizarViewHelper.carregarQuestoes(simuladoSelecionado);
            } catch (ValidacaoException ex) {
                exibirMensagem(FacesMessage.SEVERITY_ERROR, ex.getMessage());
            }
        }

        return GO_VISUALIZAR_SIMULADO;
    }

    /**
     * Limpa os campos da tela listar simulados.
     */
    private void limparTela() {
        novoSimulado = new Simulado();
        simuladoSelecionado = new Simulado();
        exibirModalTitulo = false;
    }

    /**
     * Limpa os campos da tela gerar simulado.
     */
    private void limparTelaGerarSimulado() {
        todosSelecionados = false;
        questoesSelecionadas.clear();
        questoes.clear();
    }

    /**
     * Carrega as questões disponíveis para simulado.
     */
    private void buscarQuestoes() {
        this.questoes.clear();
        this.questoesSelecionadas.clear();
        this.todosSelecionados = false;
        this.questoes = pesquisarViewHelper.pesquisar();
    }

    /**
     * Carrega os simulados do usuário.
     */
    private void buscarSimulados() {
        simulados = simuladoServico.buscarSimuladosPorCriador(usuarioLogado.getEmail());
    }

    /**
     * Seleciona uma questão da lista de questões.
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
     * Pesquisa questões disponíveis para simulado.
     */
    public void pesquisar() {
        buscarQuestoes();
        if (questoes.isEmpty()) {
            exibirMensagem(FacesMessage.SEVERITY_INFO, AvalonUtil.getInstance().getMensagem("pesquisa.sem.dados"));
        }
    }

    /**
     * Exibe uma mensagem em tela.
     */
    private void exibirMensagem(FacesMessage.Severity severity, String mensagem) {
        FacesMessage facesMessage = new FacesMessage(severity, mensagem, null);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }

    /**
     * Marca ou desmarca todas as questões disponíveis.
     */
    public void selecionarTodos() {
        questoesSelecionadas = new HashSet<>();

        for (Questao questao : questoes) {
            questao.setSelecionada(todosSelecionados);
            selecionarQuestao(questao);
        }

    }

    /**
     * Salva um novo simulado.
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
            exibirMensagem(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }

        return navegacao;
    }

    /**
     * Seleciona um simulado.
     *
     * @param simulado
     */
    public void selecionarSimulado(Simulado simulado) {
        simuladoSelecionado = simulado;
    }

    /**
     * Excluí um simulado selecionado.
     */
    public void excluir() {
        simuladoServico.remover(simuladoSelecionado);
        simulados.remove(simuladoSelecionado);
        simuladoSelecionado = null;
    }

    /**
     * Exibi o modal Título.
     */
    public void exibirModalTitulo() {
        novoSimulado = new Simulado();
        exibirModalTitulo = true;
    }
    
    /**
     * Fecha o modal Título.
     */
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

    public VisualizarSimuladoViewHelper getVisualizarViewHelper() {
        return visualizarViewHelper;
    }

    public Simulado getSimuladoSelecionado() {
        return simuladoSelecionado;
    }
    
}
