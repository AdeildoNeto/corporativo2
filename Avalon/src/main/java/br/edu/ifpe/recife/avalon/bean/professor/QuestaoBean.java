/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean.professor;

import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.questao.Alternativa;
import br.edu.ifpe.recife.avalon.model.questao.componente.ComponenteCurricular;
import br.edu.ifpe.recife.avalon.model.questao.Imagem;
import br.edu.ifpe.recife.avalon.model.questao.MultiplaEscolha;
import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.enums.TipoQuestaoEnum;
import br.edu.ifpe.recife.avalon.model.questao.VerdadeiroFalso;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.servico.ComponenteCurricularServico;
import br.edu.ifpe.recife.avalon.servico.QuestaoServico;
import br.edu.ifpe.recife.avalon.util.AvalonUtil;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author eduardoamaral
 */
@Named(value = QuestaoBean.QUESTAO_BEAN)
@SessionScoped
public class QuestaoBean implements Serializable {

    public static final String QUESTAO_BEAN = "questaoBean";
    private final String GO_LISTAR_QUESTAO = "goListarQuestao";
    private final String GO_ADD_QUESTAO = "goAddQuestao";
    private final String GO_ALTERAR_QUESTAO = "goAlterarQuestao";
    private final String MSG_PRINCIPAL = "msgPrincial";
    private final String MSG_MODAL_COMPONENTE = "msgComponente";

    @EJB
    private QuestaoServico questaoServico;

    @EJB
    private ComponenteCurricularServico componenteServico;

    private List<TipoQuestaoEnum> tipoQuestoes = new ArrayList<>();

    private TipoQuestaoEnum tipoSelecionado = TipoQuestaoEnum.DISCURSIVA;

    private List<Questao> questoes = new ArrayList<>();

    @Valid
    private Questao questao = new Questao();

    private Usuario usuarioLogado;

    HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    private List<Alternativa> alternativas = new ArrayList<>();

    private Alternativa alt1 = new Alternativa();
    private Alternativa alt2 = new Alternativa();
    private Alternativa alt3 = new Alternativa();
    private Alternativa alt4 = new Alternativa();
    private Alternativa alt5 = new Alternativa();

    @Valid
    private ComponenteCurricular novoComponenteCurricular = new ComponenteCurricular();

    private Long componenteSelecionado = null;
    private boolean exibirModalComponente;
    private boolean respostaVF;
    private Integer alternativaCorreta;
    private Imagem imagem;

    /**
     * Cria uma nova instância de <code>QuestaoBean</code>.
     */
    public QuestaoBean() {
        this.usuarioLogado = (Usuario) sessao.getAttribute("usuario");
        this.questao = new Questao();
        this.carregarTiposQuestao();

        this.alternativas = new ArrayList<>();
        alternativas.add(alt1);
        alternativas.add(alt2);
        alternativas.add(alt3);
        alternativas.add(alt4);
        alternativas.add(alt5);
    }

    /**
     * Inicializa os dados necessários para a tela de listar questões.
     *
     * @return goListarQuestao
     */
    public String iniciarPagina() {
        this.usuarioLogado = (Usuario) sessao.getAttribute("usuario");
        limparTelaInclusao();
        buscarQuestoes();
        return GO_LISTAR_QUESTAO;
    }

    /**
     * Inicializa os dados da tela de inclusão da questão.
     *
     * @return goAddQuestao
     */
    public String iniciarPaginaInclusao() {
        this.limparTelaInclusao();
        return GO_ADD_QUESTAO;
    }

    /**
     * Inicializa os dados necessários para a tela de alteração da questão.
     *
     * @param questaoSelecionada - a questão selecionada
     * @return goAlterarQuestao
     */
    public String iniciparPaginaAlteracao(Questao questaoSelecionada) {
        this.questao = questaoSelecionada;
        this.exibirModalComponente = false;
        this.componenteSelecionado = questao.getComponenteCurricular().getId();
        this.tipoSelecionado = questao.getTipo();

        if (TipoQuestaoEnum.MULTIPLA_ESCOLHA.equals(questao.getTipo())) {
            MultiplaEscolha multiplaEscolha = ((MultiplaEscolha) questao);
            this.alternativas = multiplaEscolha.getAlternativas();
            this.alternativaCorreta = multiplaEscolha.getAlternativaCorreta();
        } else if (TipoQuestaoEnum.VERDADEIRO_FALSO.equals(questao.getTipo())) {
            this.respostaVF = ((VerdadeiroFalso) questao).getResposta();
        }

        return GO_ALTERAR_QUESTAO;
    }

    /**
     * Limpa os campos da tela de inclusão.
     */
    private void limparTelaInclusao() {
        tipoSelecionado = TipoQuestaoEnum.DISCURSIVA;
        exibirModalComponente = false;
        questao = new Questao();
        componenteSelecionado = null;
        respostaVF = false;
        alternativaCorreta = null;
        imagem = null;

        limparAlternativas();
    }

    /**
     * Inicializa as alteranativas de uma questão.
     */
    private void limparAlternativas() {
        alternativas = new ArrayList<>();
        alt1 = new Alternativa();
        alt2 = new Alternativa();
        alt3 = new Alternativa();
        alt4 = new Alternativa();
        alt5 = new Alternativa();

        alternativas.add(alt1);
        alternativas.add(alt2);
        alternativas.add(alt3);
        alternativas.add(alt4);
        alternativas.add(alt5);
    }

    /**
     * Carrega as questões do usuário.
     */
    private void buscarQuestoes() {
        this.questoes = questaoServico.buscarQuestoesProfessor(usuarioLogado);
    }

    /**
     * Carrega os tipos de questão disponíveis.
     */
    private void carregarTiposQuestao() {
        this.tipoQuestoes = Arrays.asList(TipoQuestaoEnum.values());
    }

    /**
     * Salva uma nova questão.
     *
     * @return navegacao
     */
    public String salvar() {
        String navegacao;

        preencherQuestao();

        try {
            navegacao = adicionarQuestao();
            iniciarPagina();
        } catch (ValidacaoException ex) {
            exibirMensagem(ex.getMessage(), MSG_PRINCIPAL);
            navegacao = "";
        }

        return navegacao;
    }

    /**
     * Adiciona uma questão no banco de questões.
     *
     * @return
     * @throws ValidacaoException
     */
    private String adicionarQuestao() throws ValidacaoException {
        String navegacao = GO_LISTAR_QUESTAO;

        switch (tipoSelecionado) {
            case MULTIPLA_ESCOLHA:
                navegacao = salvarQuestaoMultiplaEscolha();
                break;
            case VERDADEIRO_FALSO:
                navegacao = salvarQuestaoVF();
                break;
            default:
                questaoServico.salvar(questao);
                break;
        }

        return navegacao;
    }

    /**
     * Salva uma nova questao de múltipla escolha.
     *
     * @return navegacao
     */
    private String salvarQuestaoMultiplaEscolha() throws ValidacaoException {
        String navegacao = GO_LISTAR_QUESTAO;
        MultiplaEscolha multiplaEscolha = new MultiplaEscolha();

        verificarTodasAlternativasPreenchidas();
        verificarAlternativaCorreta();
        prepararQuestaoMultiplaEscolhaSalvar(multiplaEscolha);
        questaoServico.salvar(multiplaEscolha);

        return navegacao;
    }

    /**
     * Prepara o objeto do tipo múltipla escolha para o cadastro.
     *
     * @param multiplaEscolha
     */
    private void prepararQuestaoMultiplaEscolhaSalvar(MultiplaEscolha multiplaEscolha) {
        multiplaEscolha.copiar(questao);

        alternativas.get(0).setQuestao(multiplaEscolha);
        alternativas.get(1).setQuestao(multiplaEscolha);
        alternativas.get(2).setQuestao(multiplaEscolha);
        alternativas.get(3).setQuestao(multiplaEscolha);
        alternativas.get(4).setQuestao(multiplaEscolha);

        multiplaEscolha.setAlternativas(alternativas);
        multiplaEscolha.setAlternativaCorreta(alternativaCorreta);
    }

    /**
     * Salva uma nova questão de Verdadeiro ou Falso.
     *
     * @return navegacao
     * @throws ValidacaoException
     */
    private String salvarQuestaoVF() throws ValidacaoException {
        String navegacao = GO_LISTAR_QUESTAO;

        VerdadeiroFalso verdadeiroFalso = new VerdadeiroFalso();

        verdadeiroFalso.copiar(questao);
        verdadeiroFalso.setResposta(respostaVF);

        questaoServico.salvar(verdadeiroFalso);

        return navegacao;
    }

    /**
     * Preenche a nova questão com informações básicas.
     */
    private void preencherQuestao() {
        questao.setTipo(tipoSelecionado);
        questao.setProfessor(usuarioLogado);
        questao.setDataCriacao(Calendar.getInstance().getTime());
        questao.setComponenteCurricular(buscarComponenteSelecionado());
    }

    /**
     * Salva as alterações realizadas em uma questão.
     *
     * @return navegacao
     */
    public String salvarEdicao() {
        String navegacao = GO_LISTAR_QUESTAO;

        try {
            alterarQuestao();
            iniciarPagina();
        } catch (ValidacaoException ex) {
            exibirMensagem(ex.getMessage(), MSG_PRINCIPAL);
            navegacao = "";
        }

        return navegacao;
    }

    /**
     * Altera as informações da questão conforme preenchido pelo professor.
     * 
     * @throws ValidacaoException 
     */
    private void alterarQuestao() throws ValidacaoException {
        if (questao instanceof MultiplaEscolha) {
            alterarQuestaoMultiplaEscolha();
        } else if (questao instanceof VerdadeiroFalso) {
            alterarQuestaoVerdadeiroFalso();
        } else {
            questaoServico.alterar(questao);
        }
    }

    /**
     * Altera uma questão de múltipla escolha.
     *
     * @throws ValidacaoException
     */
    private void alterarQuestaoMultiplaEscolha() throws ValidacaoException {
        verificarTodasAlternativasPreenchidas();
        verificarAlternativaCorreta();
        MultiplaEscolha multiplaEscolha = ((MultiplaEscolha) questao);
        multiplaEscolha.setAlternativaCorreta(alternativaCorreta);
        questaoServico.alterar(multiplaEscolha);
    }

    /**
     * Altera uma questão de verdadeiro ou falso.
     *
     * @throws ValidacaoException
     */
    private void alterarQuestaoVerdadeiroFalso() throws ValidacaoException {
        VerdadeiroFalso verdadeiroFalso = (VerdadeiroFalso) questao;
        verdadeiroFalso.setResposta(respostaVF);
        questaoServico.alterar(verdadeiroFalso);
    }

    /**
     * Verifica se as alternativas da questão de múltipla escolha foram
     * preenchidas.
     *
     * @throws ValidacaoException
     */
    private void verificarTodasAlternativasPreenchidas() throws ValidacaoException {
        for (Alternativa alternativa : alternativas) {
            if (alternativa.getDescricao() == null
                    || alternativa.getDescricao().isEmpty()) {
                throw new ValidacaoException(AvalonUtil.getInstance().getMensagemValidacao("questao.alternativa.obrigatorias"));
            }
        }
    }

    /**
     * Verifica se a opção correta foi definida para questões de Múltipla
     * Escolha.
     *
     * @throws ValidacaoException
     */
    private void verificarAlternativaCorreta() throws ValidacaoException {
        if (alternativaCorreta == null) {
            throw new ValidacaoException(AvalonUtil.getInstance().getMensagemValidacao("questao.resposta.obrigatoria"));
        }
    }

    /**
     * Exibi mensagens de validação em tela.
     *
     * @param mensagem
     * @param clientId
     */
    private void exibirMensagem(String mensagem, String clientId) {
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, null);
        FacesContext.getCurrentInstance().addMessage(clientId, facesMessage);
    }

    /**
     * Retorna para a página "Minhas Questões"
     *
     * @return iniciarPagina()
     */
    public String voltarListarQuestoes() {
        return iniciarPagina();
    }

    /**
     * Verifica se a questão está em modo de edição.
     *
     * @return boolean
     */
    public boolean edicaoQuestao() {
        return questao.getId() != null;
    }

    /**
     * Salva um novo componente curricular.
     */
    public void salvarComponenteCurricular() {
        try {
            componenteServico.salvar(novoComponenteCurricular);
            exibirModalComponente = false;
        } catch (ValidacaoException ex) {
            exibirMensagem(ex.getMessage(), MSG_MODAL_COMPONENTE);
        }
    }

    /**
     * Retorna todos os componentes curricular cadastrados.
     *
     * @return lista de componentes curricular
     */
    public List<ComponenteCurricular> getTodosComponentesCurricular() {
        return componenteServico.buscarTodosComponentes();
    }

    /**
     * Inicializa o modal de inserção de componente.
     */
    public void carregarModalComponente() {
        novoComponenteCurricular = new ComponenteCurricular();
        exibirModalComponente = true;
    }

    /**
     * Retorna o componente curricular selecionado.
     *
     * @return componente
     */
    private ComponenteCurricular buscarComponenteSelecionado() {
        if (componenteSelecionado == null) {
            return null;
        }

        for (ComponenteCurricular componente : getTodosComponentesCurricular()) {
            if (componenteSelecionado.equals(componente.getId())) {
                return componente;
            }
        }

        return null;
    }

    /**
     * Fecha o modal de componente curricular.
     */
    public void fecharModalComponente() {
        exibirModalComponente = false;
    }

    /**
     * Inicializa o carregamento da imagem
     *
     * @param evento
     */
    public void upload(FileUploadEvent evento) {
        setFile(evento.getFile());
    }

    /**
     * Insere os dados da imagem na questao
     *
     * @param img
     */
    private void setFile(UploadedFile img) {

        imagem = new Imagem();
        imagem.setArquivo(img.getContents());
        imagem.setExtensao(img.getContentType());
        imagem.setNome(img.getFileName());
        questao.setImagem(imagem);
    }

    public Imagem getArquivo() {
        return imagem;
    }

    /**
     * Recupera a imagem para ser exibida
     *
     * @return arquivo
     * @throws IOException
     */
    public StreamedContent getImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return new DefaultStreamedContent();
        } else {
            DefaultStreamedContent arquivo = new DefaultStreamedContent(new ByteArrayInputStream(questao.getImagem().getArquivo()));
            return arquivo;
        }
    }

    /**
     * Cancela a inserção da imagem
     *
     */
    public void limparImagem() {
        imagem = null;
        questao.setImagem(imagem);
    }

    /**
     * Selecina uma questão para anulação.
     *
     * @param questaoSelecionada
     */
    public void selecionarQuestaoAnulacao(Questao questaoSelecionada) {
        questao = questaoSelecionada;
    }

    public void anularQuestao() {
        questaoServico.anular(questao);
        questao = null;
        buscarQuestoes();
    }

    /*
        GETTERS AND SETTERS
     */
    public List<TipoQuestaoEnum> getTipoQuestoes() {
        return tipoQuestoes;
    }

    public Questao getQuestao() {
        return questao;
    }

    public TipoQuestaoEnum getTipoSelecionado() {
        return tipoSelecionado;
    }

    public void setTipoSelecionado(TipoQuestaoEnum tipoSelecionado) {
        this.tipoSelecionado = tipoSelecionado;
    }

    public List<Questao> getQuestoes() {
        return questoes;
    }

    public List<Alternativa> getAlternativas() {
        return alternativas;
    }

    public ComponenteCurricular getNovoComponenteCurricular() {
        return novoComponenteCurricular;
    }

    public Long getComponenteSelecionado() {
        return componenteSelecionado;
    }

    public void setComponenteSelecionado(Long componenteSelecionado) {
        this.componenteSelecionado = componenteSelecionado;
    }

    public boolean isExibirModalComponente() {
        return exibirModalComponente;
    }

    public void setExibirModalComponente(boolean exibirModalComponente) {
        this.exibirModalComponente = exibirModalComponente;
    }

    public boolean isRespostaVF() {
        return respostaVF;
    }

    public void setRespostaVF(boolean respostaVF) {
        this.respostaVF = respostaVF;
    }

    public Integer getAlternativaCorreta() {
        return alternativaCorreta;
    }

    public void setAlternativaCorreta(Integer alternativaCorreta) {
        this.alternativaCorreta = alternativaCorreta;
    }

}
