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
import java.util.concurrent.TimeUnit;
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
    private boolean exibirModalIniciar;
    private boolean exibirModalFinalizar;
    private String resultado;
    private String observacaoDuracao;
    private String msgConfirmarFinalizacao;
    private long duracaoMinutos;
    private long duracaoSegundos;

    /**
     * Cria uma nova instância de <code>ProvaAlunoBean</code>.
     */
    public ProvaAlunoBean() {
        usuarioLogado = (Usuario) sessao.getAttribute(USUARIO);
    }

    /**
     * Inicia a página contendo todas as provas disponíveis.
     *
     * @return rota
     */
    public String iniciarPagina() {
        limparTela();
        bucarProvasDisponiveis();
        return GO_LISTAR_PROVAS;
    }

    /**
     * Inicia uma nova Prova.
     *
     * @param provaSelecionada
     */
    public void selecionarProva(Prova provaSelecionada) {
        limparTelaProva();
        prova = provaServico.buscarProvaPorId(provaSelecionada.getId());

        if (!prova.getQuestoes().isEmpty()) {
            if (prova.getQuestoes().get(0) instanceof VerdadeiroFalso) {
                questoesVerdadeiroFalso = (List<VerdadeiroFalso>) (List<?>) prova.getQuestoes();
            } else {
                questoesMultiplaEscolha = (List<MultiplaEscolha>) (List<?>) prova.getQuestoes();
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
     * Limpa os campos da tela listar provas disponíveis.
     */
    private void limparTela() {
        fecharModalIniciar();
    }

    /**
     * Inicializa os objetos necessários para a realização da prova.
     */
    private void limparTelaProva() {
        provaAluno = new ProvaAluno();
        questoesVerdadeiroFalso.clear();
        questoesMultiplaEscolha.clear();
    }

    /**
     * Consulta todas as provas disponíveis.
     */
    private void bucarProvasDisponiveis() {
        provasDisponiveis = provaServico.buscarProvasDisponiveis(usuarioLogado);
    }

    /**
     * Inicializa os componentes necessários para realização da prova.
     *
     * @return
     */
    public String iniciarProva() {
        prepararContador();

        provaAluno = provaServico.buscarProvaAluno(usuarioLogado, prova);

        if (provaAluno.getId() == null) {
            provaAluno.setAluno(usuarioLogado);
            provaAluno.setDataHoraInicio(Calendar.getInstance().getTime());
            provaAluno.setProva(prova);
        } else {
            preencherRespostasSalvas();
        }

        fecharModalFinalizar();
        return GO_INICIAR_PROVA;
    }

    /**
     * Abri o modal para iniciar prova.
     */
    private void abrirModalIniciar() {
        exibirModalIniciar = true;
    }

    /**
     * Fecha o modal de iniciar prova.
     */
    public void fecharModalIniciar() {
        exibirModalIniciar = false;
    }

    /**
     * Carrega as observações sobre a prova.
     */
    private void carregarObservacoes() {
        observacaoDuracao = MessageFormat.format(AvalonUtil.getInstance().getMensagem(OBSERVACAO_TEMPO), prova.getDuracao());
    }

    /**
     * Verifica se há questões que ainda não foram respondidas.
     */
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
     * Finaliza a prova.
     *
     * @return rota para listar provas disponíveis.
     */
    public String finalizar() {
        preencherRespostas();

        provaAluno.setDataHoraFim(Calendar.getInstance().getTime());
        provaServico.salvarProvaAluno(provaAluno);

        return iniciarPagina();
    }

    /**
     * Carrega as repostas do usuário no histórico da prova.
     */
    private void preencherRespostas() {
        provaAluno.setQuestoesAluno(new ArrayList<ProvaAlunoQuestao>());

        if (!questoesMultiplaEscolha.isEmpty()) {
            preencherProvaMS();
        } else if (!questoesVerdadeiroFalso.isEmpty()) {
            preencherProvaVF();
        }
    }

    /**
     * Preenche as questões da prova com as respostas salvas do aluno.
     */
    private void preencherRespostasSalvas() {
        List<ProvaAlunoQuestao> questoesSalvas = provaAluno.getQuestoesAluno();
        if (questoesSalvas != null && !questoesSalvas.isEmpty()) {
            if (questoesSalvas.get(0).getQuestao() instanceof VerdadeiroFalso) {
                marcarRespostasSalvasVF(questoesSalvas);
            }else{
                marcarRespostasSalvasMS(questoesSalvas);
            }
        }
    }

    /**
     * Preenche as questões de verdadeiro ou falso da prova
     * com as respostas salvas do usuário.
     * @param questoesSalvas 
     */
    private void marcarRespostasSalvasVF(List<ProvaAlunoQuestao> questoesSalvas) {
        for (ProvaAlunoQuestao questaoSalva : questoesSalvas) {
            for (VerdadeiroFalso questao : questoesVerdadeiroFalso) {
                if(questao.getId().equals(questaoSalva.getQuestao().getId())){
                    questao.setRespostaUsuario(questaoSalva.getRespostaVF());
                }
            }
        }
    }
    
    /**
     * Preenche as questões de múltipla escolha da prova
     * com as respostas salvas do usuário.
     * @param questoesSalvas
     */
    private void marcarRespostasSalvasMS(List<ProvaAlunoQuestao> questoesSalvas) {
        for (ProvaAlunoQuestao questaoSalva : questoesSalvas) {
            for (MultiplaEscolha questao : questoesMultiplaEscolha) {
                if(questao.getId().equals(questaoSalva.getQuestao().getId())){
                    questao.setRespostaUsuario(questaoSalva.getRespostaMultiplaEscolha());
                }
            }
        }
    }

    /**
     * Salva a prova automaticamente durante a realização da mesma.
     */
    private void salvarProvaAutomaticamente() {
        preencherRespostas();
        provaAluno = provaServico.salvarProvaAluno(provaAluno);
    }

    /**
     * Preenche a prova com as questões de múltipla escolha respondidas pelo
     * aluno.
     */
    private void preencherProvaMS() {
        for (MultiplaEscolha questao : questoesMultiplaEscolha) {
            ProvaAlunoQuestao questaoAluno = new ProvaAlunoQuestao();
            questaoAluno.setProvaAluno(provaAluno);
            questaoAluno.setQuestao(questao);
            questaoAluno.setRespostaMultiplaEscolha(questao.getRespostaUsuario());
            provaAluno.getQuestoesAluno().add(questaoAluno);
        }
    }

    /**
     * Preenche a prova com as questões de verdadeiro ou falso respondidas pelo
     * aluno.
     */
    private void preencherProvaVF() {
        for (VerdadeiroFalso questao : questoesVerdadeiroFalso) {
            ProvaAlunoQuestao questaoAluno = new ProvaAlunoQuestao();
            questaoAluno.setProvaAluno(provaAluno);
            questaoAluno.setQuestao(questao);
            questaoAluno.setRespostaVF(questao.getRespostaUsuario());
            provaAluno.getQuestoesAluno().add(questaoAluno);
        }
    }

    /**
     * Verifica se todas as questões V/F foram respondidas.
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
     * Verifica se todas as questões de múltipla escolha foram respondidas.
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
     * Fecha o modal de finalizar prova.
     */
    public void fecharModalFinalizar() {
        exibirModalFinalizar = false;
    }

    /**
     * Exibi uma mensagem de erro.
     *
     * @param mensagem - mensagem a ser exibida.
     */
    private void exibirMensagemError(String mensagem) {
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, null);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }

    private void finalizarPorTempo() throws IOException {
        finalizar();
        iniciarPagina();
        String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        FacesContext.getCurrentInstance().getExternalContext().redirect(path.concat("/aluno/prova/listar.xhtml"));
    }

    /**
     * Prepara o contador da duração da prova.
     */
    public void prepararContador() {
        duracaoMinutos = TimeUnit.MILLISECONDS.toMinutes(prova.getDataHoraFim().getTime() - Calendar.getInstance().getTimeInMillis());
        duracaoSegundos = 59;

    }

    /**
     * Inicia a contagem da duração da prova.
     *
     * @throws java.io.IOException
     */
    public void iniciarContadorProva() throws IOException {
        if (duracaoMinutos == 0 && duracaoSegundos == 0) {
            finalizarPorTempo();
        }

        if (getDuracaoSegundos() == 0) {
            duracaoSegundos = 59;
            --duracaoMinutos;
            salvarProvaAutomaticamente();
        } else {
            --duracaoSegundos;
        }

    }

    /**
     * Recupera a imagem de uma questão.
     *
     * @return
     * @throws IOException
     */
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

    public long getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public long getDuracaoSegundos() {
        return duracaoSegundos;
    }

    public ProvaAluno getProvaAluno() {
        return provaAluno;
    }

}
