/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean.aluno;

import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.questao.MultiplaEscolha;
import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.VerdadeiroFalso;
import br.edu.ifpe.recife.avalon.model.simulado.Simulado;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.servico.ComponenteCurricularServico;
import br.edu.ifpe.recife.avalon.servico.QuestaoServico;
import br.edu.ifpe.recife.avalon.servico.SimuladoServico;
import br.edu.ifpe.recife.avalon.util.AvalonUtil;
import br.edu.ifpe.recife.avalon.viewhelper.ComponenteCurricularViewHelper;
import br.edu.ifpe.recife.avalon.viewhelper.PesquisarSimuladoViewHelper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpSession;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

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
    private static final String USUARIO = "usuario";
    private static final String RESULTADO_ACERTOS = "resultado.acertos";
    private static final String SIMULADO_VAZIO = "simulado.vazio";
    private static final String SIMULADO_QUESTOES_OBRIGATORIAS = "simulado.questoes.obrigatorias";
    private static final String PESQUISA_SEM_DADOS = "pesquisa.sem.dados";

    @EJB
    private QuestaoServico questaoServico;

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
     * Método para iniciar a página de geração de prova.
     *
     * @return rota para página de geração de prova
     */
    public String iniciarPagina() {
        componenteViewHelper.inicializar(componenteCurricularServico);
        pesquisarSimuladoViewHelper.inicializar(simuladoServico, usuarioLogado);
        limparTela();

        return GO_PROCURAR_SIMULADO;
    }

    /**
     * Método para iniciar um novo Simulado.
     *
     * @param simulado
     * @return navegacao
     */
    public String iniciarSimulado(Simulado simulado) {
        limparTelaSimulado();
        simuladoSelecionado = simulado;

        if (!simuladoSelecionado.getQuestoes().isEmpty()) {
            if (simuladoSelecionado.getQuestoes().get(0) instanceof VerdadeiroFalso) {
                questoesVerdadeiroFalso = (List<VerdadeiroFalso>) (List<?>) questaoServico.buscarQuestoesPorSimulado(simuladoSelecionado.getId());
            } else {
                questoesMultiplaEscolha = (List<MultiplaEscolha>) (List<?>) questaoServico.buscarQuestoesPorSimulado(simuladoSelecionado.getId());
            }

            if (questoesVerdadeiroFalso.isEmpty() && questoesMultiplaEscolha.isEmpty()) {
                exibirMensagemError(AvalonUtil.getInstance().getMensagem(SIMULADO_VAZIO));
                return null;
            }
        }

        return GO_INICIAR_SIMULADO;
    }

    /**
     * Método para limpar os campos da tela listar simulados.
     */
    private void limparTela() {
        pesquisarSimuladoViewHelper.limparFiltro();
    }

    /**
     * Método para limpar a tela do simulado.
     */
    private void limparTelaSimulado() {
        questoesVerdadeiroFalso.clear();
        questoesMultiplaEscolha.clear();
    }

    /**
     * Método para pesquisar os simulados disponíveis.
     */
    public void pesquisar() {
        simulados = pesquisarSimuladoViewHelper.pesquisar();
        if (simulados.isEmpty()) {
            exibirMensagemPesquisaSemDados();
        }
    }

    /**
     * Método para finalizar o simulado.
     */
    public void finalizar() {
        try {
            if (!questoesVerdadeiroFalso.isEmpty()) {
                verificarTodasQuestoesPreenchidasVF();
            } else {
                verificarTodasQuestoesPreenchidasMS();
            }

            calcularResultado();
            exibirModalResultado = true;
        } catch (ValidacaoException ex) {
            exibirMensagemError(ex.getMessage());
        }
    }

    /**
     * Método para verificar se todas as questões V/F foram preenchidas.
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
     * Método para verificar se todas as questões de múltipla escolha foram preenchidas.
     * @throws ValidacaoException 
     */
    public void verificarTodasQuestoesPreenchidasMS() throws ValidacaoException {
        for (MultiplaEscolha questao : questoesMultiplaEscolha) {
            if (questao.getRespostaUsuario() == null) {
                throw new ValidacaoException(AvalonUtil.getInstance().getMensagemValidacao(SIMULADO_QUESTOES_OBRIGATORIAS));
            }
        }
    }

    /**
     * Método para calcular a porcentagem de acerto do aluno no simulado.
     */
    private void calcularResultado() {
        int quantidadeQuestoes;
        int respostasCertas;

        if (!questoesVerdadeiroFalso.isEmpty()) {
            quantidadeQuestoes = questoesVerdadeiroFalso.size();
            respostasCertas = verificarRespostasVF();
        } else {
            quantidadeQuestoes = questoesMultiplaEscolha.size();
            respostasCertas = verificarRespostasMS();
        }

        resultado = MessageFormat.format(AvalonUtil.getInstance().getMensagem(RESULTADO_ACERTOS), respostasCertas, quantidadeQuestoes);
    }

    /**
     * Método para verificar a quantidade de acertos do usúario em questões de
     * V/F.
     *
     * @return quantidade de acertos
     */
    private int verificarRespostasVF() {
        int quantidadeAcertos = 0;

        for (VerdadeiroFalso questao : questoesVerdadeiroFalso) {
            if (questao.getResposta().equals(questao.getRespostaUsuario())) {
                quantidadeAcertos++;
            }
        }

        return quantidadeAcertos;
    }

    /**
     * Método para verificar a quantidade de acertos do usuário em questões de
     * múltipla escolha.
     *
     * @return quantidade de acertos
     */
    private int verificarRespostasMS() {
        int quantidadeAcertos = 0;

        for (MultiplaEscolha questao : questoesMultiplaEscolha) {
            if (questao.getOpcaoCorreta().equals(questao.getRespostaUsuario())) {
                quantidadeAcertos++;
            }
        }

        return quantidadeAcertos;
    }

    /**
     * Método para fechar o modal de resultado.
     *
     * @return navegacao
     */
    public String fecharModalResultado() {
        exibirModalResultado = false;
        return iniciarPagina();
    }

    /**
     * Método para exibição de mensagem "Pesquisa sem dados".
     */
    private void exibirMensagemPesquisaSemDados() {
        FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_INFO, AvalonUtil.getInstance().getMensagem(PESQUISA_SEM_DADOS), null);
        FacesContext.getCurrentInstance().addMessage(null, mensagem);
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
    
    public StreamedContent getImagem() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        }
        else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
            String questaoId = context.getExternalContext().getRequestParameterMap().get("questaoId");
            Questao questao = questaoServico.buscarQuestaoPorId(Long.valueOf(questaoId));
            return new DefaultStreamedContent(new ByteArrayInputStream(questao.getImagem().getArquivo()));
        }
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

}
