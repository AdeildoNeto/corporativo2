/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean.professor;

import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.questao.Alternativa;
import br.edu.ifpe.recife.avalon.model.questao.ComponenteCurricular;
import br.edu.ifpe.recife.avalon.model.questao.Imagem;
import br.edu.ifpe.recife.avalon.model.questao.MultiplaEscolha;
import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.TipoQuestaoEnum;
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
@Named(value = "questaoBean")
@SessionScoped
public class QuestaoBean implements Serializable {

    private static final String GO_LISTAR_QUESTAO = "goListarQuestao";
    private static final String GO_ADD_QUESTAO = "goAddQuestao";
    private static final String GO_ALTERAR_QUESTAO = "goAlterarQuestao";
    private static final String MSG_PRINCIPAL = "msgPrincial";
    private static final String MSG_MODAL_COMPONENTE = "msgComponente";

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
    private Integer opcaoCorreta;
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
            this.opcaoCorreta = multiplaEscolha.getOpcaoCorreta();
        }else if(TipoQuestaoEnum.VERDADEIRO_FALSO.equals(questao.getTipo())){
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
        opcaoCorreta = null;
        imagem = null;

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
        this.questoes = questaoServico.buscarQuestoesPorCriador(usuarioLogado.getEmail());
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
        String navegacao = GO_LISTAR_QUESTAO;

        preencherQuestao();

        try {

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

            limparTelaInclusao();
            buscarQuestoes();
        } catch (ValidacaoException ex) {
            exibirMensagem(ex.getMessage(), MSG_PRINCIPAL);
            navegacao = "";
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
        
        verificarOpcaoCorretaDefinida();
        copiarQuestao(multiplaEscolha);

        alternativas.get(0).setQuestao(multiplaEscolha);
        alternativas.get(1).setQuestao(multiplaEscolha);
        alternativas.get(2).setQuestao(multiplaEscolha);
        alternativas.get(3).setQuestao(multiplaEscolha);
        alternativas.get(4).setQuestao(multiplaEscolha);

        
        multiplaEscolha.setAlternativas(alternativas);
        multiplaEscolha.setOpcaoCorreta(opcaoCorreta);
        questaoServico.salvar(multiplaEscolha);

        return navegacao;
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

        copiarQuestao(verdadeiroFalso);
        verdadeiroFalso.setResposta(respostaVF);

        questaoServico.salvar(verdadeiroFalso);

        return navegacao;
    }

    /**
     * Preenche a nova questão com informações básicas.
     */
    private void preencherQuestao() {
        questao.setTipo(tipoSelecionado);
        questao.setCriador(usuarioLogado);
        questao.setDataCriacao(Calendar.getInstance().getTime());
        questao.setComponenteCurricular(buscarComponenteSelecionado());
    }

    /**
     * Copia os dados básicos da questão para instâncias do tipo Múltipla Escolha
     * e Verdadeiro ou Falso.
     * 
     * @param questao - questão que receberá os dados básicos.
     */
    private void copiarQuestao(Questao questao) {
        questao.setEnunciado(this.questao.getEnunciado());
        questao.setCriador(this.questao.getCriador());
        questao.setTipo(this.questao.getTipo());
        questao.setDataCriacao(this.questao.getDataCriacao());
        questao.setComponenteCurricular(this.questao.getComponenteCurricular());
        questao.setCompartilhada(this.questao.getCompartilhada());
        questao.setImagem(this.questao.getImagem());
    }

    /**
     * Salva as alterações realizadas em uma questão.
     * 
     * @return navegacao
     */
    public String salvarEdicao() {
        String navegacao = GO_LISTAR_QUESTAO;

        try {
            questao.setComponenteCurricular(buscarComponenteSelecionado());
            if(questao instanceof MultiplaEscolha){
                verificarOpcaoCorretaDefinida();
                MultiplaEscolha multiplaEscolha = ((MultiplaEscolha) questao);
                multiplaEscolha.setOpcaoCorreta(opcaoCorreta);
                questaoServico.alterar(multiplaEscolha);
            }else if(questao instanceof VerdadeiroFalso){
                VerdadeiroFalso verdadeiroFalso = (VerdadeiroFalso) questao;
                verdadeiroFalso.setResposta(respostaVF);
                questaoServico.alterar(verdadeiroFalso);
            } else {
                questaoServico.alterar(questao);
            }
            limparTelaInclusao();
            buscarQuestoes();
        } catch (ValidacaoException ex) {
            exibirMensagem(ex.getMessage(), MSG_PRINCIPAL);
            navegacao = "";
        }

        return navegacao;
    }
    
    /**
     * Verifica se a opção correta foi definida para questões
     * de Múltipla Escolha.
     * 
     * @throws ValidacaoException 
     */
    private void verificarOpcaoCorretaDefinida() throws ValidacaoException{
        if(opcaoCorreta == null){
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
     * Seleciona uma questão da lista de questões.
     *
     * @param questaoSelecionada
     */
    public void selecionarQuestao(Questao questaoSelecionada) {
        this.questao = questaoSelecionada;
    }

    /**
     * Exclui uma questão selecionada.
     */
    public void excluir() {
        questaoServico.remover(questao);
        questoes.remove(questao);
        questao = null;
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
     * @param evento
     */
    public void upload(FileUploadEvent evento) {
        setFile(evento.getFile());
    }

    /**
     * Insere os dados da imagem na questao
     * @param img
     */
    private void setFile(UploadedFile img) {
        
        imagem = new Imagem();
        imagem.setArquivo(img.getContents());
        imagem.setExtensao(img.getContentType());
        imagem.setNome(img.getFileName());
        questao.setImagem(imagem);       
    }
    
    /**
     * Recupera a imagem para ser exibida
     * @return arquivo
     * @throws IOException
     */
    public StreamedContent getImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return new DefaultStreamedContent();
        }
        else {           
           DefaultStreamedContent arquivo = new DefaultStreamedContent(new ByteArrayInputStream(questao.getImagem().getArquivo()));
            return arquivo;
        }
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

    public Integer getOpcaoCorreta() {
        return opcaoCorreta;
    }

    public void setOpcaoCorreta(Integer opcaoCorreta) {
        this.opcaoCorreta = opcaoCorreta;
    }

}
