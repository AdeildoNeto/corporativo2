/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean;

import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.questao.Alternativa;
import br.edu.ifpe.recife.avalon.model.questao.ComponenteCurricular;
import br.edu.ifpe.recife.avalon.model.questao.MultiplaEscolha;
import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.TipoQuestaoEnum;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.servico.ComponenteCurricularServico;
import br.edu.ifpe.recife.avalon.servico.QuestaoServico;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
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
    
    private List<Alternativa> alternativas = new ArrayList<Alternativa>();

    private Alternativa alt1 = new Alternativa();
    private Alternativa alt2 = new Alternativa();
    private Alternativa alt3 = new Alternativa();
    private Alternativa alt4 = new Alternativa();
    private Alternativa alt5 = new Alternativa();
    
    @Valid
    private ComponenteCurricular novoComponenteCurricular = new ComponenteCurricular();
    
    private Long componenteSelecionado = null;
    
    private boolean exibirModalComponente = false;

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
        this.exibirModalComponente = false;
        this.componenteSelecionado = questao.getComponenteCurricular().getId();
        this.tipoSelecionado = questao.getTipo();

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
        questao.setComponenteCurricular(buscarComponenteSelecionado());

        try {
            questaoServico.validarEnunciadoPorTipoValido(questao);

            if (TipoQuestaoEnum.MULTIPLA_ESCOLHA.equals(tipoSelecionado)) {
                return salvarQuestaoMultiplaEscolha();
            } else {
                questaoServico.salvar(questao);
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
            questaoMultipla.setComponenteCurricular(buscarComponenteSelecionado());
            questaoMultipla.setCompartilhada(questao.getCompartilhada());

            alternativas.get(0).setQuestao(questaoMultipla);
            alternativas.get(1).setQuestao(questaoMultipla);
            alternativas.get(2).setQuestao(questaoMultipla);
            alternativas.get(3).setQuestao(questaoMultipla);
            alternativas.get(4).setQuestao(questaoMultipla);

            questaoMultipla.setAlternativas(alternativas);
            questaoServico.salvar(questaoMultipla);

            limparTelaInclusao();
            buscarQuestoes();

        } catch (ValidacaoException ex) {
            exibirMensagem(ex.getMessage(), MSG_PRINCIPAL);
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

            questao.setComponenteCurricular(buscarComponenteSelecionado());
            questaoServico.alterar(questao);
            limparTelaInclusao();
            buscarQuestoes();
        } catch (ValidacaoException ex) {
            exibirMensagem(ex.getMessage(), MSG_PRINCIPAL);
            navegacao = "";
        }

        return navegacao;
    }
    
    /**
     * Método para exibição de mensagens de validação.
     * 
     * @param mensagem
     * @param clientId 
     */
    private void exibirMensagem(String mensagem, String clientId) {
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, null);
        FacesContext.getCurrentInstance().addMessage(clientId, facesMessage);
    }

    /**
     * Método para limpar os campos da tela.
     */
    private void limparTelaInclusao() {
        tipoSelecionado = TipoQuestaoEnum.DISCURSIVA;
        exibirModalComponente = false;
        questao = new Questao();
        componenteSelecionado = null;

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

    /**
     * Método para retornar a página "Minhas Questões"
     * @return iniciarPagina()
     */
    public String voltarListarQuestoes() {
        return iniciarPagina();
    }
    
    /**
     * Método para checar se a questão está em modo edição.
     * @return boolean
     */
    public boolean edicaoQuestao(){
        return questao.getId() != null;
    }
    
    /**
     * Método para salvar um componente curricular.
     */
    public void salvarComponenteCurricular(){
        try {
            componenteServico.salvar(novoComponenteCurricular);
            exibirModalComponente = false;
        } catch (ValidacaoException ex) {
            exibirMensagem(ex.getMessage(), MSG_MODAL_COMPONENTE);
        }
    }
    
    /**
     * Método para buscar todos os componentes curricular cadastrados.
     * @return lista de componentes curricular
     */
    public List<ComponenteCurricular> getTodosComponentesCurricular(){
        return componenteServico.buscarTodosComponentes();
    }
    
    /**
     * Método para carregar o modal de inserção de componente.
     */
    public void carregarModalComponente(){
        novoComponenteCurricular = new ComponenteCurricular();
        exibirModalComponente = true;
    }
    
    /**
     * Método para buscar o componente curricular selecionado.
     * @return componente
     */
    private ComponenteCurricular buscarComponenteSelecionado(){
        if(componenteSelecionado == null){
            return null;
        }
        
        for(ComponenteCurricular componente : getTodosComponentesCurricular()){
            if(componenteSelecionado.equals(componente.getId())){
                return componente;
            }
        }
        
        return null;
    }
    
    /**
     * Método para fechar o modal de componente curricular.
     */
    public void fecharModalComponente(){
        exibirModalComponente = false;
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
    
    public List<Alternativa> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(List<Alternativa> alternativas) {
        this.alternativas = alternativas;
    }

    public ComponenteCurricular getNovoComponenteCurricular() {
        return novoComponenteCurricular;
    }

    public void setNovoComponenteCurricular(ComponenteCurricular novoComponenteCurricular) {
        this.novoComponenteCurricular = novoComponenteCurricular;
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
    
}
