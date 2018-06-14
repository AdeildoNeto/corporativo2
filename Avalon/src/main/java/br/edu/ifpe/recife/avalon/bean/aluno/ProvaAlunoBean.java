/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean.aluno;

import br.edu.ifpe.recife.avalon.model.prova.Prova;
import br.edu.ifpe.recife.avalon.model.prova.ProvaAluno;
import br.edu.ifpe.recife.avalon.model.prova.ProvaAlunoQuestao;
import br.edu.ifpe.recife.avalon.model.questao.MultiplaEscolha;
import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.VerdadeiroFalso;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.servico.ProvaServico;
import br.edu.ifpe.recife.avalon.servico.QuestaoServico;
import br.edu.ifpe.recife.avalon.util.AvalonUtil;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
@Named(value = ProvaAlunoBean.NOME)
@SessionScoped
public class ProvaAlunoBean implements Serializable {

    public static final String NOME = "provaAlunoBean";
    private static final String GO_INICIAR_PROVA = "goIniciarProva";
    private static final String GO_LISTAR_PROVAS = "goListarProvas";
    private static final String USUARIO = "usuario";
    private static final String PROVA_QUESTOES_EM_BRANCO = "prova.mensagem.questoes.em.branco";
    private static final String PROVA_FINALIZAR = "prova.mensagem.finalizar";
    private static final String OBSERVACAO_TEMPO = "prova.observacao.tempo";

    @EJB
    private QuestaoServico questaoServico;

    @EJB
    private ProvaServico provaServico;

    private final HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private final Usuario usuarioLogado;

    private Prova prova;
    private ProvaAluno provaAluno = new ProvaAluno();
    private List<Prova> provasDisponiveis = new ArrayList<>();
    private List<VerdadeiroFalso> questoesVerdadeiroFalso = new ArrayList<>();
    private List<MultiplaEscolha> questoesMultiplaEscolha = new ArrayList<>();
    private boolean exibirModalResultado;
    private boolean exibirModalIniciar;
    private boolean exibirModalFinalizar;
    private String resultado;
    private String observacaoDuracao;
    private String msgConfirmarFinalizacao;
    private long duracaoMinutos;
    private long duracaoSegundos;

    /**
     * Cria uma nova instância de <code>SimuladoAlunoBean</code>.
     */
    public ProvaAlunoBean() {
        usuarioLogado = (Usuario) sessao.getAttribute(USUARIO);
    }

    /**
     * Método para iniciar a página contendo todas as provas disponíveis.
     *
     * @return rota
     */
    public String iniciarPagina() {
        limparTela();
        bucarProvasDisponiveis();
        return GO_LISTAR_PROVAS;
    }

    /**
     * Método para iniciar uma nova Prova.
     *
     * @param provaSelecionada
     * @return rota
     */
    public void selecionarProva(Prova provaSelecionada) {
        limparTelaSimulado();
        prova = provaSelecionada;

        if (!prova.getQuestoes().isEmpty()) {
            if (prova.getQuestoes().get(0) instanceof VerdadeiroFalso) {
                questoesVerdadeiroFalso = (List<VerdadeiroFalso>) (List<?>) questaoServico.buscarQuestoesPorProva(prova.getId());
            } else {
                questoesMultiplaEscolha = (List<MultiplaEscolha>) (List<?>) questaoServico.buscarQuestoesPorProva(prova.getId());
            }

            if (questoesVerdadeiroFalso.isEmpty() && questoesMultiplaEscolha.isEmpty()) {
                exibirMensagemError("Ocorreu um erro ao realizar esta ação.");
            } else {
                carregarObservacoes();
                abrirModalIniciar();
            }
        }
    }

    /**
     * Método para limpar os campos da tela listar simulados.
     */
    private void limparTela() {
        fecharModalIniciar();
    }

    /**
     * Método para limpar a tela do simulado.
     */
    private void limparTelaSimulado() {
        questoesVerdadeiroFalso.clear();
        questoesMultiplaEscolha.clear();
    }

    private void bucarProvasDisponiveis() {
        provasDisponiveis = provaServico.buscarProvasDisponiveis(usuarioLogado);
    }

    public String iniciarProva() {
        prepararContador();

        provaAluno.setAluno(usuarioLogado);
        provaAluno.setDataHoraInicio(Calendar.getInstance().getTime());
        provaAluno.setProva(prova);
        fecharModalFinalizar();
        return GO_INICIAR_PROVA;
    }

    private void abrirModalIniciar() {
        exibirModalIniciar = true;
    }

    public void fecharModalIniciar() {
        exibirModalIniciar = false;
    }

    private void carregarObservacoes() {
        observacaoDuracao = MessageFormat.format(AvalonUtil.getInstance().getMensagem(OBSERVACAO_TEMPO), prova.getDuracao());
    }

    public void verificarQuestoesEmBranco() {
        exibirModalFinalizar = true;

        if (!questoesVerdadeiroFalso.isEmpty() && verificarTodasQuestoesPreenchidasVF()
                || !questoesMultiplaEscolha.isEmpty() && verificarTodasQuestoesPreenchidasMS()) {
            msgConfirmarFinalizacao = AvalonUtil.getInstance().getMensagem(PROVA_QUESTOES_EM_BRANCO);
        } else {
            msgConfirmarFinalizacao = AvalonUtil.getInstance().getMensagem(PROVA_FINALIZAR);
        }
    }

    /**
     * Método para finalizar o simulado.
     */
    public String finalizar() {
        double nota;
        preencherRespostas();

        nota = calcularNota();
        provaAluno.setNota(nota);
        provaAluno.setDataHoraFim(Calendar.getInstance().getTime());
        provaServico.salvarProvaAluno(provaAluno);

        //salvar resultado da prova
        return GO_LISTAR_PROVAS;
    }

    public void preencherRespostas() {
        List<ProvaAlunoQuestao> questoes = new ArrayList<>();

        if (!questoesMultiplaEscolha.isEmpty()) {
            for (MultiplaEscolha questao : questoesMultiplaEscolha) {
                ProvaAlunoQuestao questaoAluno = new ProvaAlunoQuestao();
                questaoAluno.setProvaAluno(provaAluno);
                questaoAluno.setQuestao(questao);
                questaoAluno.setRespostaMultiplaEscolha(questao.getRespostaUsuario());
                questoes.add(questaoAluno);
            }
        } else if (!questoesVerdadeiroFalso.isEmpty()) {
            for (VerdadeiroFalso questao : questoesVerdadeiroFalso) {
                ProvaAlunoQuestao questaoAluno = new ProvaAlunoQuestao();
                questaoAluno.setProvaAluno(provaAluno);
                questaoAluno.setQuestao(questao);
                questaoAluno.setRespostaVF(questao.getRespostaUsuario());
                questoes.add(questaoAluno);
            }
        }

        provaAluno.setQuestoesAluno(questoes);
    }

    /**
     * Método para verificar se todas as questões V/F foram preenchidas.
     *
     * @return
     */
    public boolean verificarTodasQuestoesPreenchidasVF() {
        for (VerdadeiroFalso questao : questoesVerdadeiroFalso) {
            if (questao.getRespostaUsuario() == null) {
                return true;
            }
        }

        return false;
    }

    /**
     * Método para verificar se todas as questões de múltipla escolha foram
     * preenchidas.
     *
     * @return
     */
    public boolean verificarTodasQuestoesPreenchidasMS() {
        for (MultiplaEscolha questao : questoesMultiplaEscolha) {
            if (questao.getRespostaUsuario() == null) {
                return true;
            }
        }

        return false;
    }

    /**
     * Método para calcular a porcentagem de acerto do aluno no simulado.
     */
    private double calcularNota() {
        double quantidadeQuestoes;
        double respostasCertas;

        if (!questoesVerdadeiroFalso.isEmpty()) {
            quantidadeQuestoes = questoesVerdadeiroFalso.size();
            respostasCertas = verificarRespostasVF();
        } else {
            quantidadeQuestoes = questoesMultiplaEscolha.size();
            respostasCertas = verificarRespostasMS();
        }

        return (respostasCertas / quantidadeQuestoes) * 10.0;
    }

    /**
     * Método para verificar a quantidade de acertos do usúario em questões de
     * V/F.
     *
     * @return quantidade de acertos
     */
    private double verificarRespostasVF() {
        double quantidadeAcertos = 0;

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
    private double verificarRespostasMS() {
        double quantidadeAcertos = 0;

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

    public void fecharModalFinalizar() {
        exibirModalFinalizar = false;
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

    public void prepararContador() {

        setDuracaoMinutos(prova.getDuracao() - 1);
        setDuracaoSegundos(59);

    }

    public void iniciarContadorProva() {

        if (getDuracaoSegundos() == 0) {
            setDuracaoSegundos(59);
            setDuracaoMinutos(getDuracaoMinutos() - 1);
        } else {
            setDuracaoSegundos(getDuracaoSegundos() - 1);
        }

    }

    public StreamedContent getImagem() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        } else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
            String questaoId = context.getExternalContext().getRequestParameterMap().get("questaoId");
            Questao questao = questaoServico.buscarQuestaoPorId(Long.valueOf(questaoId));
            return new DefaultStreamedContent(new ByteArrayInputStream(questao.getImagem().getArquivo()));
        }
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

    public Prova getProva() {
        return prova;
    }

    public List<Prova> getProvasDisponiveis() {
        return provasDisponiveis;
    }

    public boolean isExibirModalIniciar() {
        return exibirModalIniciar;
    }

    public String getObservacaoDuracao() {
        return observacaoDuracao;
    }

    public boolean isExibirModalFinalizar() {
        return exibirModalFinalizar;
    }

    public String getMsgConfirmarFinalizacao() {
        return msgConfirmarFinalizacao;
    }

    /**
     * @return the duracaoMinutos
     */
    public long getDuracaoMinutos() {
        return duracaoMinutos;
    }

    /**
     * @param duracaoMinutos the duracaoMinutos to set
     */
    public void setDuracaoMinutos(long duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
    }

    /**
     * @return the duracaoSegundos
     */
    public long getDuracaoSegundos() {
        return duracaoSegundos;
    }

    /**
     * @param duracaoSegundos the duracaoSegundos to set
     */
    public void setDuracaoSegundos(long duracaoSegundos) {
        this.duracaoSegundos = duracaoSegundos;
    }

}
