/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean.professor;

import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.questao.MultiplaEscolha;
import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.VerdadeiroFalso;
import br.edu.ifpe.recife.avalon.model.simulado.Simulado;
import br.edu.ifpe.recife.avalon.model.simulado.SimuladoAluno;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.servico.ComponenteCurricularServico;
import br.edu.ifpe.recife.avalon.servico.QuestaoServico;
import br.edu.ifpe.recife.avalon.servico.SimuladoServico;
import br.edu.ifpe.recife.avalon.util.AvalonUtil;
import br.edu.ifpe.recife.avalon.viewhelper.ComponenteCurricularViewHelper;
import br.edu.ifpe.recife.avalon.viewhelper.PesquisarQuestaoViewHelper;
import br.edu.ifpe.recife.avalon.viewhelper.QuestaoDetalhesViewHelper;
import br.edu.ifpe.recife.avalon.viewhelper.VisualizarAvaliacaoViewHelper;
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
    private static final String GO_SIMULADO_RESULTADOS = "goResultadosSimulado";
    private static final String USUARIO = "usuario";

    @EJB
    private QuestaoServico questaoServico;

    @EJB
    private SimuladoServico simuladoServico;

    @EJB
    private ComponenteCurricularServico componenteCurricularServico;

    private final ComponenteCurricularViewHelper componenteViewHelper;
    private final QuestaoDetalhesViewHelper detalhesViewHelper;
    private final PesquisarQuestaoViewHelper pesquisarQuestoesViewHelper;
    private final VisualizarAvaliacaoViewHelper visualizarViewHelper;
    private final HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private final Usuario usuarioLogado;

    private Simulado simulado;
    private List<Questao> questoes;
    private List<Simulado> simulados;
    private Set<Questao> questoesSelecionadas;
    private boolean todosSelecionados;
    private boolean exibirModalTitulo;
    private boolean exibirModalExclusao;
    private String titulo;
    private final String headerModalTitulo;
    
    private List<SimuladoAluno> resultados;
    private SimuladoAluno simuladoAlunoDetalhe;
    private Simulado simuladoResultadoSelecionada;

    /**
     * Cria uma nova instância de <code>SimuladoBean</code>.
     */
    public SimuladoBean() {
        usuarioLogado = (Usuario) sessao.getAttribute(USUARIO);
        componenteViewHelper = new ComponenteCurricularViewHelper();
        detalhesViewHelper = new QuestaoDetalhesViewHelper();
        pesquisarQuestoesViewHelper = new PesquisarQuestaoViewHelper();
        visualizarViewHelper = new VisualizarAvaliacaoViewHelper();
        questoes = new ArrayList<>();
        questoesSelecionadas = new HashSet<>();
        simulado = new Simulado();
        headerModalTitulo = AvalonUtil.getInstance().getMensagem("simulado.novo");
    }

    /**
     * Inicializa os dados necessários para a página de geração de prova.
     *
     * @return rota para página de geração de prova
     */
    public String iniciarPagina() {
        limparPagina();
        buscarSimulados();

        return GO_LISTAR_SIMULADO;
    }

    /**
     * Inicializa os dados necessários para p[agina gerar novo simulado.
     *
     * @return navegação.
     */
    public String iniciarPaginaGerar() {
        componenteViewHelper.inicializar(componenteCurricularServico);
        detalhesViewHelper.inicializar();
        pesquisarQuestoesViewHelper.inicializar(questaoServico, usuarioLogado, true);
        limparTelaGerarSimulado();
        simulado.setTitulo(titulo);

        return GO_GERAR_SIMULADO;
        
    }
    
    /**
     * Inicializa a página de resultados de um simulado.
     * 
     * @param simulado
     * @return 
     */
    public String iniciarPaginaResultados(Simulado simulado){
        simuladoResultadoSelecionada = simulado;
        buscarResultados(simulado);
        
        return GO_SIMULADO_RESULTADOS;
    }

    /**
     * Inicializa os dados necessários para a página visualizar simulado.
     *
     * @param simuladoSelecionado - simulado selecionado.
     * @return navegacao
     */
    public String iniciarPaginaVisualizarSimulado(Simulado simuladoSelecionado) {
        simulado = simuladoSelecionado;

        if (!simulado.getQuestoes().isEmpty()) {
            try {

                if (simuladoSelecionado.getQuestoes().get(0) instanceof VerdadeiroFalso) {
                    visualizarViewHelper.setQuestoesVerdadeiroFalso((List<VerdadeiroFalso>) (List<?>) simulado.getQuestoes());
                } else {
                    visualizarViewHelper.setQuestoesMultiplaEscolha((List<MultiplaEscolha>) (List<?>) simulado.getQuestoes());
                }

                if (visualizarViewHelper.getQuestoesMultiplaEscolha().isEmpty() && visualizarViewHelper.getQuestoesVerdadeiroFalso().isEmpty()) {
                    throw new ValidacaoException(AvalonUtil.getInstance().getMensagem("simulado.vazio"));
                }

            } catch (ValidacaoException ex) {
                exibirMensagem(FacesMessage.SEVERITY_ERROR, ex.getMessage());
            }
        }

        return GO_VISUALIZAR_SIMULADO;
    }

    /**
     * Limpa os campos da tela listar simulados.
     */
    private void limparPagina() {
        simulado = new Simulado();
        titulo = null;
        fecharModalTitulo();
        fecharModalExclusao();
    }

    /**
     * Limpa os campos da tela gerar simulado.
     */
    private void limparTelaGerarSimulado() {
        simulado = new Simulado();
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
        this.questoes = pesquisarQuestoesViewHelper.pesquisar();
    }

    /**
     * Carrega os simulados do usuário.
     */
    private void buscarSimulados() {
        simulados = simuladoServico.buscarSimuladosPorCriador(usuarioLogado.getEmail());
    }

    /**
     * Carrega todos os resultados dos alunos em um simulado.
     * 
     * @param simulado 
     */
    private void buscarResultados(Simulado simulado){
        resultados = simuladoServico.buscarResultadosSimulado(simulado);
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
            simulado.setComponenteCurricular(componenteViewHelper.getComponenteCurricularPorId(pesquisarQuestoesViewHelper.getFiltro().getIdComponenteCurricular()));
            simulado.setDataCriacao(Calendar.getInstance().getTime());
            simulado.setCriador(usuarioLogado);
            simulado.setQuestoes(new ArrayList<Questao>());
            simulado.getQuestoes().addAll(questoesSelecionadas);
            simuladoServico.salvar(simulado);
            navegacao = iniciarPagina();
        } catch (ValidacaoException ex) {
            exibirMensagem(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }

        return navegacao;
    }

    /**
     * Seleciona um simulado para exclusão.
     *
     * @param simuladoSelecionado
     */
    public void selecionarSimuladoExclusao(Simulado simuladoSelecionado) {
        simulado = simuladoSelecionado;
        exibirModalExclusao();
    }

    /**
     * Excluí um simulado selecionado.
     */
    public void excluir() {
        simuladoServico.remover(simulado);
        simulados.remove(simulado);
        simulado = null;
    }

    /**
     * Exibi o modal Título.
     */
    public void exibirModalTitulo() {
        titulo = null;
        exibirModalTitulo = true;
    }

    /**
     * Fecha o modal Título.
     */
    public void fecharModalTitulo() {
        exibirModalTitulo = false;
    }

    /**
     * Exibi o modal de exclusão.
     */
    private void exibirModalExclusao() {
        exibirModalExclusao = true;
    }

    /**
     * Fecha o modal de exclusão.
     */
    public void fecharModalExclusao() {
        exibirModalExclusao = false;
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

    public Simulado getSimulado() {
        return simulado;
    }

    public ComponenteCurricularViewHelper getComponenteViewHelper() {
        return componenteViewHelper;
    }

    public QuestaoDetalhesViewHelper getDetalhesViewHelper() {
        return detalhesViewHelper;
    }

    public PesquisarQuestaoViewHelper getPesquisarQuestoesViewHelper() {
        return pesquisarQuestoesViewHelper;
    }

    public List<Simulado> getSimulados() {
        return simulados;
    }

    public boolean isExibirModalTitulo() {
        return exibirModalTitulo;
    }

    public VisualizarAvaliacaoViewHelper getVisualizarViewHelper() {
        return visualizarViewHelper;
    }

    public boolean isExibirModalExclusao() {
        return exibirModalExclusao;
    }

    public String getHeaderModalTitulo() {
        return headerModalTitulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Simulado getSimuladoResultadoSelecionada() {
        return simuladoResultadoSelecionada;
    }

    public List<SimuladoAluno> getResultados() {
        return resultados;
    }

    public SimuladoAluno getSimuladoAlunoDetalhe() {
        return simuladoAlunoDetalhe;
    }

}
