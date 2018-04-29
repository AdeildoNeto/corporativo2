/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean;

import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.helper.ComponenteCurricularHelper;
import br.edu.ifpe.recife.avalon.model.questao.Alternativa;
import br.edu.ifpe.recife.avalon.model.questao.MultiplaEscolha;
import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.TipoQuestaoEnum;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.servico.QuestaoServico;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 *
 * @author eduardo.f.amaral
 */
@ManagedBean
@SessionScoped
public class QuestaoBean implements Serializable {

    private static final String GO_LISTAR_QUESTAO = "goListarQuestao";
    private static final String GO_ADD_QUESTAO = "goAddQuestao";
    private static final String GO_ALTERAR_QUESTAO = "goAlterarQuestao";

    private List<Alternativa> alternativas = new ArrayList<Alternativa>();

    private Alternativa alt1 = new Alternativa();
    private Alternativa alt2 = new Alternativa();
    private Alternativa alt3 = new Alternativa();
    private Alternativa alt4 = new Alternativa();
    private Alternativa alt5 = new Alternativa();

    @EJB
    private QuestaoServico questaoServico;

    @ManagedProperty(value = "#{componenteCurricularHelper}")
    private ComponenteCurricularHelper componenteCurricularHelper = new ComponenteCurricularHelper();
    
    private List<TipoQuestaoEnum> tipoQuestoes = new ArrayList<>();

    private TipoQuestaoEnum tipoSelecionado = TipoQuestaoEnum.DISCURSIVA;

    private List<Questao> questoes = new ArrayList<>();

    @Valid
    private Questao questao = new Questao();

    private boolean exibirModalConfirmarExclusao = false;

    private Usuario usuarioLogado;

    HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

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

    @PostConstruct
    private void init() {
        iniciarPagina();
    }

    /**
     * Método para inicializar variáveis utilizadas na tela Listar Questões.
     *
     * @return
     */
    public String iniciarPagina() {
        this.usuarioLogado = (Usuario) sessao.getAttribute("usuario");
        buscarQuestoes();
        return GO_LISTAR_QUESTAO;
    }

    /**
     * Método para inicializar as variáveis da tela de inclusão de questão.
     *
     * @return goAddQuestao
     */
    public String iniciarPaginaInclusao() {
        this.usuarioLogado = (Usuario) sessao.getAttribute("usuario");
        this.limparTelaInclusao();
        return GO_ADD_QUESTAO;
    }

    /**
     * Método para inicializar as variáveis da tela de alteração da questão.
     *
     * @param questaoSelecionada
     * @return goAlterarQuestao
     */
    public String iniciparPaginaAlteracao(Questao questaoSelecionada) {
        this.questao = questaoSelecionada;

        if (TipoQuestaoEnum.MULTIPLA_ESCOLHA.equals(questaoSelecionada.getTipo())) {
            this.alternativas = ((MultiplaEscolha) questaoSelecionada).getAlternativas();
        }

        return GO_ALTERAR_QUESTAO;
    }

    /**
     * Método para carregar as questões do usuário.
     */
    private void buscarQuestoes() {
        this.questoes = questaoServico.buscarQuestoesPorCriador(usuarioLogado.getEmail());
    }

    /**
     * Método responsável por carregar os tipos de questão disponíveis.
     */
    private void carregarTiposQuestao() {
        this.tipoQuestoes.add(TipoQuestaoEnum.DISCURSIVA);
        this.tipoQuestoes.add(TipoQuestaoEnum.MULTIPLA_ESCOLHA);
        this.tipoQuestoes.add(TipoQuestaoEnum.VERDADEIRO_FALSO);
    }

    /**
     * Método responsável por enviar ao servico a Questão à salvar.
     *
     * @return rota da próxima tela.
     */
    public String salvar() {
        String navegacao = GO_LISTAR_QUESTAO;
        
        questao.setTipo(tipoSelecionado);
        questao.setCriador(usuarioLogado);
        questao.setDataCriacao(Calendar.getInstance().getTime());

        try {
            questaoServico.validarEnunciadoPorTipoValido(questao);

            if (TipoQuestaoEnum.MULTIPLA_ESCOLHA.equals(tipoSelecionado)) {
                return salvarQuestaoMultiplaEscolha();
            } else {
                questaoServico.salvar(questao);
            }
            limparTelaInclusao();
            buscarQuestoes();
        } catch (ValidacaoException | EJBException ex) {
            exibirMensagem(ex.getMessage());
            navegacao = "";
        }

        return navegacao;
    }

    /**
     * Método para salvar uma questao de múltipla escolha.
     *
     * @return nav
     */
    private String salvarQuestaoMultiplaEscolha() {
        String navegacao = GO_LISTAR_QUESTAO;
        
        try {
            questaoServico.validarAlternativasDiferentes(alternativas);

            MultiplaEscolha questaoMultipla = new MultiplaEscolha();

            questaoMultipla.setEnunciado(questao.getEnunciado());
            questaoMultipla.setCriador(questao.getCriador());
            questaoMultipla.setTipo(TipoQuestaoEnum.MULTIPLA_ESCOLHA);
            questaoMultipla.setDataCriacao(questao.getDataCriacao());

            alternativas.get(0).setQuestao(questaoMultipla);
            alternativas.get(1).setQuestao(questaoMultipla);
            alternativas.get(2).setQuestao(questaoMultipla);
            alternativas.get(3).setQuestao(questaoMultipla);
            alternativas.get(4).setQuestao(questaoMultipla);

            questaoMultipla.setAlternativas(alternativas);
            questaoServico.salvar(questaoMultipla);

            limparTelaInclusao();
            buscarQuestoes();

        } catch (ValidacaoException | EJBException ex) {
            exibirMensagem(ex.getMessage());
            navegacao = "";
        }
        
        return navegacao;
    }

    /**
     * Método para salvar as alterações realizadas em uma questão.
     *
     * @return nav
     */
    public String salvarEdicao() {
        String navegacao = GO_LISTAR_QUESTAO;
        
        try {

            questaoServico.valirEnunciadoPorTipoValidoEdicao(questao);

            if (TipoQuestaoEnum.MULTIPLA_ESCOLHA.equals(questao.getTipo())) {
                questaoServico.validarAlternativasDiferentes(alternativas);
            }

            questaoServico.alterar(questao);
            limparTelaInclusao();
            buscarQuestoes();
        } catch (ValidacaoException | EJBException ex) {
            exibirMensagem(ex.getMessage());
            navegacao = "";
        }

        return navegacao;
    }

    /**
     * Método para exibição de mensagens.
     */
    private void exibirMensagem(String mensagem) {
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, null);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }

    /**
     * Método para limpar os campos da tela.
     */
    private void limparTelaInclusao() {
        tipoSelecionado = TipoQuestaoEnum.DISCURSIVA;
        questao = new Questao();

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
     * Método para selecionar uma questão da lista de questões.
     *
     * @param questaoSelecionada
     */
    public void selecionarQuestao(Questao questaoSelecionada) {
        this.questao = questaoSelecionada;
    }

    /**
     * Método para excluir uma questão
     */
    public void excluir() {
        questaoServico.remover(questao);
        questoes.remove(questao);
        questao = null;
    }

    public String voltarListarQuestoes() {
        return iniciarPagina();
    }
    
    public boolean edicaoQuestao(){
        return questao.getId() != null;
    }

    /*
        GETTERS AND SETTERS
     */
    public List<TipoQuestaoEnum> getTipoQuestoes() {
        return tipoQuestoes;
    }

    public void setTipoQuestoes(List<TipoQuestaoEnum> tipoQuestoes) {
        this.tipoQuestoes = tipoQuestoes;
    }

    public Questao getQuestao() {
        return questao;
    }

    public void setQuestao(Questao questao) {
        this.questao = questao;
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

    public void setQuestoes(List<Questao> questoes) {
        this.questoes = questoes;
    }

    public boolean isExibirModalConfirmarExclusao() {
        return exibirModalConfirmarExclusao;
    }

    public void setExibirModalConfirmarExclusao(boolean exibirModalConfirmarExclusao) {
        this.exibirModalConfirmarExclusao = exibirModalConfirmarExclusao;
    }

    public List<Alternativa> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(List<Alternativa> alternativas) {
        this.alternativas = alternativas;
    }

    public ComponenteCurricularHelper getComponenteCurricularHelper() {
        return componenteCurricularHelper;
    }

    public void setComponenteCurricularHelper(ComponenteCurricularHelper componenteCurricularHelper) {
        this.componenteCurricularHelper = componenteCurricularHelper;
    }
    
}
