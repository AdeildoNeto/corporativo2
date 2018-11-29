/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean.comum;

import br.edu.ifpe.recife.avalon.bean.professor.*;
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author eduardoamaral
 */
public class AbstractSimuladoBean extends AvaliacaoBean {

    @EJB
    protected QuestaoServico questaoServico;

    @EJB
    protected SimuladoServico simuladoServico;

    @EJB
    protected ComponenteCurricularServico componenteCurricularServico;

    protected final ComponenteCurricularViewHelper componenteViewHelper = new ComponenteCurricularViewHelper();
    protected final QuestaoDetalhesViewHelper detalhesViewHelper = new QuestaoDetalhesViewHelper();
    protected final VisualizarAvaliacaoViewHelper visualizarViewHelper = new VisualizarAvaliacaoViewHelper();
    protected final HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    protected Usuario usuarioLogado;

    protected Simulado simulado = new Simulado();
    protected List<Simulado> simulados;
    protected boolean exibirModalExclusao;

    protected List<SimuladoAluno> resultados;
    protected SimuladoAluno simuladoAlunoResultado;
    protected Simulado simuladoResultadoSelecionado;
    protected boolean simuladoVF;

    protected final List<QuestaoSimulado> questoesSimulado = new ArrayList<>();
    protected final List<QuestaoSimulado> questoesSimuladoSelecionadas = new ArrayList<>();

    /**
     * Inicializa os dados necessários para p[agina gerar novo simulado.
     *
     */
    protected void inicializarPaginaGerar() {
        FiltroQuestao filtro = inicializarFiltro();

        componenteViewHelper.inicializar(componenteCurricularServico);
        detalhesViewHelper.inicializar();
        getPesquisarQuestoesViewHelper().inicializar(questaoServico, filtro);
        limparTelaGerarSimulado();
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
     */
    public void inicializarPaginaResultados(Simulado simulado) {
        simuladoResultadoSelecionado = simulado;
        buscarResultados(simulado);
    }

    /**
     * Inicializa os dados necessários para a página visualizar simulado.
     *
     * @param simuladoSelecionado - simulado selecionado.
     */
    public void inicializarPaginaVisualizarSimulado(Simulado simuladoSelecionado) {
        simulado = simuladoSelecionado;

        if (!simulado.getQuestoes().isEmpty()) {

            if (isSimuladoVerdadeiroFalso()) {
                carregarQuestoesVerdadeiroFalso();
            } else {
                carregarQuetoesMultiplaEscolha();
            }

        }
    }

    /**
     * Inicializa a página de detalhes de um resultado.
     *
     * @param simuladoAluno
     */
    protected void inicializarPaginaDetalhar(SimuladoAluno simuladoAluno) {
        simuladoAlunoResultado = simuladoAluno;
        simuladoVF = simuladoAlunoResultado.getSimulado().getQuestoes().get(0).getQuestao() instanceof VerdadeiroFalso;
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
     * Limpa os campos da tela listar simulados.
     */
    protected void limparPagina() {
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
     * @param navegacao
     * @return
     */
    protected String salvarSimulado(String navegacao) {

        try {
            simulado.setComponenteCurricular(componenteViewHelper.getComponenteCurricularPorId(getPesquisarQuestoesViewHelper().getFiltro().getIdComponenteCurricular()));
            simulado.setDataCriacao(Calendar.getInstance().getTime());
            simulado.setProfessor(usuarioLogado);
            simulado.setQuestoes(new ArrayList<QuestaoSimulado>());
            simulado.getQuestoes().addAll(questoesSimuladoSelecionadas);
            simuladoServico.salvar(simulado);
        } catch (ValidacaoException ex) {
            navegacao = null;
            Logger.getLogger(AbstractSimuladoBean.class.getName()).log(Level.SEVERE, null, ex);
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
        fecharModalExclusao();
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
