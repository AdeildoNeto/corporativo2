/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean.aluno;

import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.avaliacao.QuestaoAvaliacao;
import br.edu.ifpe.recife.avalon.model.questao.MultiplaEscolha;
import br.edu.ifpe.recife.avalon.model.questao.VerdadeiroFalso;
import br.edu.ifpe.recife.avalon.model.avaliacao.simulado.Simulado;
import br.edu.ifpe.recife.avalon.model.avaliacao.simulado.SimuladoAluno;
import br.edu.ifpe.recife.avalon.model.avaliacao.simulado.QuestaoAlunoSimulado;
import br.edu.ifpe.recife.avalon.model.avaliacao.simulado.QuestaoSimulado;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.servico.ComponenteCurricularServico;
import br.edu.ifpe.recife.avalon.servico.SimuladoServico;
import br.edu.ifpe.recife.avalon.util.AvalonUtil;
import br.edu.ifpe.recife.avalon.viewhelper.ComponenteCurricularViewHelper;
import br.edu.ifpe.recife.avalon.viewhelper.PesquisarSimuladoViewHelper;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
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
@Named(value = SimuladoAlunoBean.NOME)
@SessionScoped
public class SimuladoAlunoBean implements Serializable {

    public static final String NOME = "simuladoAlunoBean";
    private static final String GO_INICIAR_SIMULADO = "goIniciarSimulado";
    private static final String GO_PROCURAR_SIMULADO = "goProcurarSimulado";
    private static final String GO_LISTAR_RESULTADOS_SIMULADO_ALUNO = "goListarResultadosSimuladoAluno";
    private static final String GO_DETALHAR_RESULTADO_SIMULADO_ALUNO = "goDetalharResultadoSimuladoAluno";
    private static final String USUARIO = "usuario";
    private static final String RESULTADO_ACERTOS = "resultado.obtido";
    private static final String SIMULADO_QUESTOES_OBRIGATORIAS = "simulado.questoes.obrigatorias";
    private static final String PESQUISA_SEM_DADOS = "pesquisa.sem.dados";

    @EJB
    private SimuladoServico simuladoServico;

    @EJB
    private ComponenteCurricularServico componenteCurricularServico;

    private final ComponenteCurricularViewHelper componenteViewHelper;
    private final PesquisarSimuladoViewHelper pesquisarSimuladoViewHelper;
    private final HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private final Usuario usuarioLogado;

    private Simulado simuladoSelecionado;
    private List<Simulado> simulados;
    private List<VerdadeiroFalso> questoesVerdadeiroFalso;
    private List<MultiplaEscolha> questoesMultiplaEscolha;
    private boolean exibirModalResultado;
    private String resultado;
    private SimuladoAluno simuladoAluno;
    
    private List<SimuladoAluno> resultados = new ArrayList<>();
    private SimuladoAluno resultadoSimuladoAluno =  new SimuladoAluno();
    private boolean simuladoVF;

    /**
     * Cria uma nova instância de <code>SimuladoAlunoBean</code>.
     */
    public SimuladoAlunoBean() {
        usuarioLogado = (Usuario) sessao.getAttribute(USUARIO);
        componenteViewHelper = new ComponenteCurricularViewHelper();
        pesquisarSimuladoViewHelper = new PesquisarSimuladoViewHelper();
        questoesVerdadeiroFalso = new ArrayList<>();
        questoesMultiplaEscolha = new ArrayList<>();
        simulados = new ArrayList<>();
    }

    /**
     * Inicia a página listar simulados disponíveis.
     *
     * @return rota
     */
    public String iniciarPagina() {
        componenteViewHelper.inicializar(componenteCurricularServico);
        pesquisarSimuladoViewHelper.inicializar(simuladoServico, usuarioLogado);
        limparTela();

        return GO_PROCURAR_SIMULADO;
    }

    /**
     * Inicia um novo Simulado.
     *
     * @param simulado
     * @return navegacao
     */
    public String iniciarSimulado(Simulado simulado) {
        limparTelaSimulado();
        simuladoSelecionado = simulado;
        simuladoAluno = new SimuladoAluno();
        simuladoAluno.setAluno(usuarioLogado);
        simuladoAluno.setSimulado(simuladoSelecionado);
        simuladoAluno.setDataHoraInicio(Calendar.getInstance().getTime());

        questoesMultiplaEscolha.clear();
        questoesVerdadeiroFalso.clear();

        return carregarQuestoes();
    }
    
    public String iniciarPaginaResultadosSimulado(Simulado simulado) {
        resultados = simuladoServico.buscarResultadosSimuladoAluno(usuarioLogado, simulado);
        return GO_LISTAR_RESULTADOS_SIMULADO_ALUNO;
    }
    
    public String iniciarPaginaDetalharResultado(SimuladoAluno simuladoAluno){
        resultadoSimuladoAluno = simuladoAluno;
        simuladoVF = resultadoSimuladoAluno.getSimulado().getQuestoes().get(0).getQuestao() instanceof VerdadeiroFalso;
        return GO_DETALHAR_RESULTADO_SIMULADO_ALUNO;
    }

    private String carregarQuestoes() {
        String navegacao = GO_INICIAR_SIMULADO;

        if (!simuladoSelecionado.getQuestoes().isEmpty()) {
            if (isSimuladoVerdadeiroFalso()) {
                questoesVerdadeiroFalso = carregarQuestoesVerdadeiroFalso();
            } else {
                questoesMultiplaEscolha = carregarQuestoesMultiplaEscolha();
            }
        }

        return navegacao;
    }

    private boolean isSimuladoVerdadeiroFalso() {
        return simuladoSelecionado.getQuestoes().get(0).getQuestao() instanceof VerdadeiroFalso;
    }

    /**
     * Carrega as questões de verdadeiro ou falso de um simulado.
     *
     * @return
     */
    private List<VerdadeiroFalso> carregarQuestoesVerdadeiroFalso() {
        List<VerdadeiroFalso> questoes = new ArrayList<>();
        for (QuestaoSimulado questaoSimulado : simuladoSelecionado.getQuestoes()) {
            questoes.add((VerdadeiroFalso) questaoSimulado.getQuestao());
        }
        return questoes;
    }

    /**
     * Carrega as questões de múltipla escolha de um simulado.
     *
     * @return
     */
    private List<MultiplaEscolha> carregarQuestoesMultiplaEscolha() {
        List<MultiplaEscolha> questoes = new ArrayList<>();
        for (QuestaoSimulado questaoSimulado : simuladoSelecionado.getQuestoes()) {
            questoes.add((MultiplaEscolha) questaoSimulado.getQuestao());
        }
        return questoes;
    }

    /**
     * Limpa os campos da tela listar simulados.
     */
    private void limparTela() {
        pesquisarSimuladoViewHelper.limparFiltro();
    }

    /**
     * Limpa os campos da tela do simulado.
     */
    private void limparTelaSimulado() {
        questoesVerdadeiroFalso.clear();
        questoesMultiplaEscolha.clear();
    }

    /**
     * Pesquisa os simulados disponíveis.
     */
    public void pesquisar() {
        simulados = pesquisarSimuladoViewHelper.pesquisar();
        if (simulados.isEmpty()) {
            exibirMensagem(FacesMessage.SEVERITY_INFO, AvalonUtil.getInstance().getMensagem(PESQUISA_SEM_DADOS));
        }
    }

    /**
     * Finaliza o simulado.
     */
    public void finalizar() {
        try {
            simuladoAluno.setQuestoesAluno(new ArrayList<QuestaoAlunoSimulado>());

            if (!questoesVerdadeiroFalso.isEmpty()) {
                verificarTodasQuestoesPreenchidasVF();
                preencherSimuladoVF();
            } else {
                verificarTodasQuestoesPreenchidasMultiplaEscolha();
                preencherSimuladoMultiplaEscolha();
            }

            exibirResultado(simuladoAluno.getNota());
            simuladoAluno.setDataHoraFim(Calendar.getInstance().getTime());
            simuladoServico.salvarSimuladoAluno(simuladoAluno);
        } catch (ValidacaoException ex) {
            exibirMensagem(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }
    }

    /**
     * Preenche a prova com as questões de verdadeiro ou falso respondidas pelo
     * aluno.
     */
    private void preencherSimuladoVF() {
        for (VerdadeiroFalso verdadeiroFalso : questoesVerdadeiroFalso) {
            QuestaoAlunoSimulado questao = new QuestaoAlunoSimulado();
            questao.setQuestaoAvaliacao(carregarQuestaoAvaliacao(verdadeiroFalso.getId()));
            questao.setRespostaVF(verdadeiroFalso.getRespostaUsuario());
            questao.setSimuladoAluno(simuladoAluno);
            simuladoAluno.getQuestoesAluno().add(questao);
        }
    }

    /**
     * Recupera a questaoAvaliacao de um simulado.
     *
     * @param idQuestao
     * @return
     */
    private QuestaoAvaliacao carregarQuestaoAvaliacao(Long idQuestao) {
        for (QuestaoSimulado questaoSimulado : simuladoSelecionado.getQuestoes()) {
            if (questaoSimulado.getQuestao().getId().equals(idQuestao)) {
                return questaoSimulado;
            }
        }
        return null;
    }

    /**
     * Preenche a prova com as questões de múltipla escolha respondidas pelo
     * aluno.
     */
    private void preencherSimuladoMultiplaEscolha() {
        for (MultiplaEscolha multiplaEscolha : questoesMultiplaEscolha) {
            QuestaoAlunoSimulado questao = new QuestaoAlunoSimulado();
            questao.setQuestaoAvaliacao(carregarQuestaoAvaliacao(multiplaEscolha.getId()));
            questao.setRespostaMultiplaEscolha(multiplaEscolha.getRespostaUsuario());
            questao.setSimuladoAluno(simuladoAluno);
            simuladoAluno.getQuestoesAluno().add(questao);
        }
    }

    /**
     * Verificar se todas as questões V/F foram preenchidas.
     *
     * @throws br.edu.ifpe.recife.avalon.excecao.ValidacaoException
     */
    public void verificarTodasQuestoesPreenchidasVF() throws ValidacaoException {
        for (VerdadeiroFalso questao : questoesVerdadeiroFalso) {
            if (questao.getRespostaUsuario() == null) {
                throw new ValidacaoException(AvalonUtil.getInstance().getMensagemValidacao(SIMULADO_QUESTOES_OBRIGATORIAS));
            }
        }
    }

    /**
     * Verifica se todas as questões de múltipla escolha foram preenchidas.
     *
     * @throws ValidacaoException
     */
    public void verificarTodasQuestoesPreenchidasMultiplaEscolha() throws ValidacaoException {
        for (MultiplaEscolha questao : questoesMultiplaEscolha) {
            if (questao.getRespostaUsuario() == null) {
                throw new ValidacaoException(AvalonUtil.getInstance().getMensagemValidacao(SIMULADO_QUESTOES_OBRIGATORIAS));
            }
        }
    }

    /**
     * Exibi a nota obitida pelo aluno no simulado.
     */
    private void exibirResultado(Double nota) {
        BigDecimal notaFormatada = BigDecimal.valueOf(nota).setScale(2, RoundingMode.UP);
        resultado = MessageFormat.format(AvalonUtil.getInstance().getMensagem(RESULTADO_ACERTOS), notaFormatada);
        exibirModalResultado = true;
    }

    /**
     * Fecha o modal de resultado.
     *
     * @return navegacao
     */
    public String fecharModalResultado() {
        exibirModalResultado = false;
        return iniciarPagina();
    }

    /**
     * Exibi uma mensagem.
     *
     * @param mensagem - mensagem a ser exibida.
     */
    private void exibirMensagem(FacesMessage.Severity severity, String mensagem) {
        FacesMessage facesMessage = new FacesMessage(severity, mensagem, null);
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

    public void setSimuladoSelecionado(Simulado simuladoSelecionado) {
        this.simuladoSelecionado = simuladoSelecionado;
    }

    public List<MultiplaEscolha> getQuestoesMultiplaEscolha() {
        return questoesMultiplaEscolha;
    }

    public List<VerdadeiroFalso> getQuestoesVerdadeiroFalso() {
        return questoesVerdadeiroFalso;
    }

    public boolean isExibirModalResultado() {
        return exibirModalResultado;
    }

    public String getResultado() {
        return resultado;
    }

    public List<SimuladoAluno> getResultados() {
        return resultados;
    }

    public SimuladoAluno getResultadoSimuladoAluno() {
        return resultadoSimuladoAluno;
    }

    public boolean isSimuladoVF() {
        return simuladoVF;
    }
    
}
