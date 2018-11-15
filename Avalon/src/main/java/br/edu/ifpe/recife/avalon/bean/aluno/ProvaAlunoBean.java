/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean.aluno;

import br.edu.ifpe.recife.avalon.model.avaliacao.QuestaoAvaliacao;
import br.edu.ifpe.recife.avalon.model.avaliacao.prova.Prova;
import br.edu.ifpe.recife.avalon.model.avaliacao.prova.ProvaAluno;
import br.edu.ifpe.recife.avalon.model.avaliacao.prova.QuestaoAlunoProva;
import br.edu.ifpe.recife.avalon.model.avaliacao.prova.QuestaoProva;
import br.edu.ifpe.recife.avalon.model.questao.MultiplaEscolha;
import br.edu.ifpe.recife.avalon.model.questao.VerdadeiroFalso;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.servico.ProvaServico;
import br.edu.ifpe.recife.avalon.util.AvalonUtil;
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
import javax.servlet.http.HttpSession;

/**
 *
 * @author eduardoamaral
 */
@Named(value = ProvaAlunoBean.NOME)
@SessionScoped
public class ProvaAlunoBean implements Serializable {

    public static final String NOME = "provaAlunoBean";
    
    private static final String USUARIO = "usuario";
    
    private static final String GO_INICIAR_PROVA = "goIniciarProva";
    private static final String GO_LISTAR_PROVAS = "goListarProvas";
    private static final String GO_LISTAR_RESULTADO = "goListarResultados";
    private static final String GO_DETALHAR_RESULTADO_ALUNO = "goDetalharResultadoAluno";
    
    private static final String PROVA_QUESTOES_EM_BRANCO = "prova.mensagem.questoes.em.branco";
    private static final String PROVA_FINALIZAR = "prova.mensagem.finalizar";
    private static final String OBSERVACAO_TEMPO = "prova.observacao.tempo";

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
    
    private List<ProvaAluno> provasResultados = new ArrayList<>();
    private ProvaAluno provaAlunoResultado = new ProvaAluno();
    private boolean provaVF;

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
     * Inicia a página contendo todas as provas realizadas pelo aluno e seus
     * respectivos resultados.
     *
     * @return rota
     */
    public String iniciarPaginaResultado() {
        buscarProvasResultados();
        return GO_LISTAR_RESULTADO;
    }
    
    /**
     * Inicializa a prova selecionada para detalha-la.
     *
     * @param provaSelecionada
     * @return rota
     */
    public String iniciarPaginaDetalhar(ProvaAluno provaSelecionada) {
        provaAlunoResultado = provaSelecionada;

        if (!provaAlunoResultado.getProva().getQuestoes().isEmpty()) {
            provaVF = provaAlunoResultado.getProva().getQuestoes().get(0).getQuestao() instanceof VerdadeiroFalso;
            return GO_DETALHAR_RESULTADO_ALUNO;
        }

        return null;
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
            if (isProvaVerdadeiroFalso()) {
                questoesVerdadeiroFalso = carregarQuestoesVerdadeiroFalso();
            } else {
                questoesMultiplaEscolha = carregarQuestoesMultiplaEscolha();
            }

            carregarObservacoes();
            abrirModalIniciar();
        }
    }

    private boolean isProvaVerdadeiroFalso() {
        return prova.getQuestoes().get(0).getQuestao() instanceof VerdadeiroFalso;
    }

    /**
     * Carrega as questões de verdadeiro ou falso de um simulado.
     *
     * @return
     */
    private List<VerdadeiroFalso> carregarQuestoesVerdadeiroFalso() {
        List<VerdadeiroFalso> questoes = new ArrayList<>();
        for (QuestaoProva questaoProva : prova.getQuestoes()) {
            questoes.add((VerdadeiroFalso) questaoProva.getQuestao());
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
        for (QuestaoProva questaoProva : prova.getQuestoes()) {
            questoes.add((MultiplaEscolha) questaoProva.getQuestao());
        }
        return questoes;
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
                || !questoesMultiplaEscolha.isEmpty() && verificarTodasQuestoesPreenchidasMultiplaEscolha()) {
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
        provaAluno.setQuestoesAluno(new ArrayList<QuestaoAlunoProva>());

        if (!questoesMultiplaEscolha.isEmpty()) {
            preencherProvaMultiplaEscolha();
        } else if (!questoesVerdadeiroFalso.isEmpty()) {
            preencherProvaVF();
        }
    }

    /**
     * Preenche as questões da prova com as respostas salvas do aluno.
     */
    private void preencherRespostasSalvas() {
        List<QuestaoAlunoProva> questoesSalvas = provaAluno.getQuestoesAluno();
        if (questoesSalvas != null && !questoesSalvas.isEmpty()) {
            if (isQuestaoAlunoVerdadeiroFalso(questoesSalvas.get(0))) {
                marcarRespostasSalvasVF(questoesSalvas);
            } else {
                marcarRespostasSalvasMultiplaEscolha(questoesSalvas);
            }
        }
    }

    private boolean isQuestaoAlunoVerdadeiroFalso(QuestaoAlunoProva questaoAluno) {
        return questaoAluno.getQuestaoAvaliacao().getQuestao() instanceof VerdadeiroFalso;
    }

    /**
     * Preenche as questões de verdadeiro ou falso da prova com as respostas
     * salvas do usuário.
     *
     * @param questoesSalvas
     */
    private void marcarRespostasSalvasVF(List<QuestaoAlunoProva> questoesSalvas) {
        for (QuestaoAlunoProva questaoSalva : questoesSalvas) {
            for (VerdadeiroFalso questao : questoesVerdadeiroFalso) {
                if (questao.getId().equals(questaoSalva.getQuestaoAvaliacao().getId())) {
                    questao.setRespostaUsuario(questaoSalva.getRespostaVF());
                }
            }
        }
    }

    /**
     * Preenche as questões de múltipla escolha da prova com as respostas salvas
     * do usuário.
     *
     * @param questoesSalvas
     */
    private void marcarRespostasSalvasMultiplaEscolha(List<QuestaoAlunoProva> questoesSalvas) {
        for (QuestaoAlunoProva questaoSalva : questoesSalvas) {
            for (MultiplaEscolha questao : questoesMultiplaEscolha) {
                if (questao.getId().equals(questaoSalva.getQuestaoAvaliacao().getId())) {
                    questao.setRespostaUsuario(questaoSalva.getRespostaMultiplaEscolha());
                }
            }
        }
    }

    /**
     * Salva a prova automaticamente durante a realização da mesma.
     */
    public void salvarProvaAutomaticamente() {
        preencherRespostas();
        provaAluno = provaServico.salvarProvaAluno(provaAluno);
    }

    /**
     * Preenche a prova com as questões de múltipla escolha respondidas pelo
     * aluno.
     */
    private void preencherProvaMultiplaEscolha() {
        for (MultiplaEscolha questaoME : questoesMultiplaEscolha) {
            QuestaoAlunoProva questaoAluno = new QuestaoAlunoProva();
            questaoAluno.setProvaAluno(provaAluno);
            questaoAluno.setQuestaoAvaliacao(carregarQuestaoAvaliacao(questaoME.getId()));
            questaoAluno.setRespostaMultiplaEscolha(questaoME.getRespostaUsuario());
            provaAluno.getQuestoesAluno().add(questaoAluno);
        }
    }

    /**
     * Recupera a questaoAvaliacao de uma prova.
     *
     * @param idQuestao
     * @return
     */
    private QuestaoAvaliacao carregarQuestaoAvaliacao(Long idQuestao) {
        for (QuestaoProva questaoProva : prova.getQuestoes()) {
            if(questaoProva.getQuestao().getId().equals(idQuestao)){
                return questaoProva;
            }
        }
        return null;
    }

    /**
     * Preenche a prova com as questões de verdadeiro ou falso respondidas pelo
     * aluno.
     */
    private void preencherProvaVF() {
        for (VerdadeiroFalso questaoVF : questoesVerdadeiroFalso) {
            QuestaoAlunoProva questaoAluno = new QuestaoAlunoProva();
            questaoAluno.setProvaAluno(provaAluno);
            questaoAluno.setQuestaoAvaliacao(carregarQuestaoAvaliacao(questaoVF.getId()));
            questaoAluno.setRespostaVF(questaoVF.getRespostaUsuario());
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
    public boolean verificarTodasQuestoesPreenchidasMultiplaEscolha() {
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
        } else {
            --duracaoSegundos;
        }

    }
    
    /**
     * Busca por todos as provas realizadas pelo aluon e seus respectivos
     * resultados.
     */
    private void buscarProvasResultados() {
        provasResultados = provaServico.buscarResultadosProvasAluno(usuarioLogado);
    }

    public boolean isProvaVF() {
        return provaVF;
    }

    public List<ProvaAluno> getProvasResultados() {
        return provasResultados;
    }

    public ProvaAluno getProvaAlunoResultado() {
        return provaAlunoResultado;
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
