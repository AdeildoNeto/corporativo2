/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean.professor;

import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.filtro.FiltroQuestao;
import br.edu.ifpe.recife.avalon.model.questao.MultiplaEscolha;
import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.VerdadeiroFalso;
import br.edu.ifpe.recife.avalon.model.avaliacao.simulado.QuestaoSimulado;
import br.edu.ifpe.recife.avalon.model.avaliacao.simulado.Simulado;
import br.edu.ifpe.recife.avalon.model.avaliacao.simulado.SimuladoAluno;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.servico.ComponenteCurricularServico;
import br.edu.ifpe.recife.avalon.servico.QuestaoServico;
import br.edu.ifpe.recife.avalon.servico.SimuladoServico;
import br.edu.ifpe.recife.avalon.util.AvalonUtil;
import br.edu.ifpe.recife.avalon.viewhelper.ComponenteCurricularViewHelper;
import br.edu.ifpe.recife.avalon.viewhelper.QuestaoDetalhesViewHelper;
import br.edu.ifpe.recife.avalon.viewhelper.VisualizarAvaliacaoViewHelper;
import java.util.ArrayList;
import java.util.Calendar;
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
@Named(value = SimuladoBean.NOME)
@SessionScoped
public class SimuladoBean extends AvaliacaoBean {

    public static final String NOME = "simuladoBean";
    private static final String GO_GERAR_SIMULADO = "goGerarSimulado";
    private static final String GO_LISTAR_SIMULADO = "goLisarSimulado";
    private static final String GO_VISUALIZAR_SIMULADO = "goVisualizarSimulado";
    private static final String GO_SIMULADO_RESULTADOS = "goResultadosSimulado";
    private static final String GO_DETALHAR_RESULTADO = "goDetalharResultadoSimulado";
    private static final String USUARIO = "usuario";

    @EJB
    private QuestaoServico questaoServico;

    @EJB
    private SimuladoServico simuladoServico;

    @EJB
    private ComponenteCurricularServico componenteCurricularServico;

    private final ComponenteCurricularViewHelper componenteViewHelper;
    private final QuestaoDetalhesViewHelper detalhesViewHelper;
    private final VisualizarAvaliacaoViewHelper visualizarViewHelper;
    private final HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private final Usuario usuarioLogado;

    private Simulado simulado;
    private List<Simulado> simulados;
    private boolean exibirModalExclusao;

    private List<SimuladoAluno> resultados;
    private SimuladoAluno simuladoAlunoResultado;
    private Simulado simuladoResultadoSelecionado;
    private boolean simuladoVF;

    private final List<QuestaoSimulado> questoesSimulado;
    private final List<QuestaoSimulado> questoesSimuladoSelecionadas;

    /**
     * Cria uma nova instância de <code>SimuladoBean</code>.
     */
    public SimuladoBean() {
        usuarioLogado = (Usuario) sessao.getAttribute(USUARIO);
        componenteViewHelper = new ComponenteCurricularViewHelper();
        detalhesViewHelper = new QuestaoDetalhesViewHelper();
        visualizarViewHelper = new VisualizarAvaliacaoViewHelper();
        simulado = new Simulado();
        questoesSimulado = new ArrayList<>();
        questoesSimuladoSelecionadas = new ArrayList<>();
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
        FiltroQuestao filtro = inicializarFiltro();

        componenteViewHelper.inicializar(componenteCurricularServico);
        detalhesViewHelper.inicializar();
        getPesquisarQuestoesViewHelper().inicializar(questaoServico, filtro);
        limparTelaGerarSimulado();

        return GO_GERAR_SIMULADO;

    }

    private FiltroQuestao inicializarFiltro() {
        FiltroQuestao filtro = new FiltroQuestao(usuarioLogado.getEmail(), true);
        filtro.setQuestaoSimulado(true);

        return filtro;
    }

    /**
     * Inicializa a página de resultados de um simulado.
     *
     * @param simulado
     * @return
     */
    public String iniciarPaginaResultados(Simulado simulado) {
        simuladoResultadoSelecionado = simulado;
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

            if (isSimuladoVerdadeiroFalso()) {
                carregarQuestoesVerdadeiroFalso();
            } else {
                carregarQuetoesMultiplaEscolha();
            }

        }

        return GO_VISUALIZAR_SIMULADO;
    }

    private boolean isSimuladoVerdadeiroFalso() {
        return simulado.getQuestoes().get(0).getQuestao() instanceof VerdadeiroFalso;
    }

    /**
     * Carrega as questões de verdadeiro ou falso do simulado para detalhá-lo.
     */
    private void carregarQuestoesVerdadeiroFalso() {
        List<VerdadeiroFalso> questoesVF = new ArrayList<>();

        for (QuestaoSimulado questaoSimulado : simulado.getQuestoes()) {
            questoesVF.add((VerdadeiroFalso) questaoSimulado.getQuestao());
        }

        visualizarViewHelper.setQuestoesVerdadeiroFalso(questoesVF);
    }

    /**
     * Carrega as questões de múltipla escolha do simulado para detalhá-lo.
     */
    private void carregarQuetoesMultiplaEscolha() {
        List<MultiplaEscolha> questoesME = new ArrayList<>();

        for (QuestaoSimulado questaoSimulado : simulado.getQuestoes()) {
            questoesME.add((MultiplaEscolha) questaoSimulado.getQuestao());
        }

        visualizarViewHelper.setQuestoesMultiplaEscolha(questoesME);
    }

    /**
     * Inicializa a página de detalhes de um resultado.
     *
     * @param simuladoAluno
     * @return rota
     */
    public String iniciarPaginaDetalhar(SimuladoAluno simuladoAluno) {
        simuladoAlunoResultado = simuladoAluno;

        simuladoVF = simuladoAlunoResultado.getSimulado().getQuestoes().get(0).getQuestao() instanceof VerdadeiroFalso;
        return GO_DETALHAR_RESULTADO;
    }

    /**
     * Limpa os campos da tela listar simulados.
     */
    private void limparPagina() {
        simulado = new Simulado();
        fecharModalExclusao();
    }

    /**
     * Limpa os campos da tela gerar simulado.
     */
    private void limparTelaGerarSimulado() {
        simulado = new Simulado();
        inicializarQuestoes();
    }
    
    private void inicializarQuestoes() {
        super.setTodosSelecionados(false);
        questoesSimuladoSelecionadas.clear();
        questoesSimulado.clear();
    }

    /**
     * Carrega os simulados do usuário.
     */
    private void buscarSimulados() {
        simulados = simuladoServico.buscarSimuladosProfessor(usuarioLogado);
    }

    /**
     * Carrega todos os resultados dos alunos em um simulado.
     *
     * @param simulado
     */
    private void buscarResultados(Simulado simulado) {
        resultados = simuladoServico.buscarResultadosSimulado(simulado);
    }
    
    /**
     * Pesquisa as questões disponíveis para impressão.
     */
    public void pesquisar() {
        buscarQuestoes();
        if (questoesSimulado.isEmpty()) {
            exibirMensagem(FacesMessage.SEVERITY_INFO, AvalonUtil.getInstance().getMensagem("pesquisa.sem.dados"));
        }
    }
    
    /**
     * Carrega as as questões do a partir do filtro informado.
     */
    private void buscarQuestoes() {
        inicializarQuestoes();
        for (Questao questao : super.getPesquisarQuestoesViewHelper().pesquisar()) {
            QuestaoSimulado questaoSimulado = new QuestaoSimulado();
            questaoSimulado.setQuestao(questao);
            questaoSimulado.setSimulado(simulado);
            questoesSimulado.add(questaoSimulado);
        }
    }

    /**
     * Seleciona uma questão da lista de questões.
     *
     * @param questaoSimulado - questão de simulado selecionada.
     */
    public void selecionarQuestaoSimulado(QuestaoSimulado questaoSimulado) {
        if (questaoSimulado.getQuestao().isSelecionada()) {
            questoesSimuladoSelecionadas.add(questaoSimulado);
        } else {
            questoesSimuladoSelecionadas.remove(questaoSimulado);
            super.setTodosSelecionados(false);
        }
    }

    /**
     * Marca ou desmarca todas as questões disponíveis para gerar um simulado
     * online.
     */
    public void selecionarTodasQuestoesSimulado() {
        questoesSimuladoSelecionadas.clear();

        for (QuestaoSimulado questaoSimulado : questoesSimulado) {
            questaoSimulado.getQuestao().setSelecionada(super.isTodosSelecionados());
            selecionarQuestaoSimulado(questaoSimulado);
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
            simulado.setComponenteCurricular(componenteViewHelper.getComponenteCurricularPorId(getPesquisarQuestoesViewHelper().getFiltro().getIdComponenteCurricular()));
            simulado.setDataCriacao(Calendar.getInstance().getTime());
            simulado.setProfessor(usuarioLogado);
            simulado.setQuestoes(new ArrayList<QuestaoSimulado>());
            simulado.getQuestoes().addAll(questoesSimuladoSelecionadas);
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

    public Simulado getSimulado() {
        return simulado;
    }

    public ComponenteCurricularViewHelper getComponenteViewHelper() {
        return componenteViewHelper;
    }

    public QuestaoDetalhesViewHelper getDetalhesViewHelper() {
        return detalhesViewHelper;
    }

    public List<Simulado> getSimulados() {
        return simulados;
    }

    public VisualizarAvaliacaoViewHelper getVisualizarViewHelper() {
        return visualizarViewHelper;
    }

    public boolean isExibirModalExclusao() {
        return exibirModalExclusao;
    }

    public Simulado getSimuladoResultadoSelecionado() {
        return simuladoResultadoSelecionado;
    }

    public List<SimuladoAluno> getResultados() {
        return resultados;
    }

    public SimuladoAluno getSimuladoAlunoResultado() {
        return simuladoAlunoResultado;
    }

    public boolean isSimuladoVF() {
        return simuladoVF;
    }

    public List<QuestaoSimulado> getQuestoesSimulado() {
        return questoesSimulado;
    }

    public List<QuestaoSimulado> getQuestoesSimuladoSelecionadas() {
        return questoesSimuladoSelecionadas;
    }
    
}
