/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean.professor;

import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.questao.Alternativa;
import br.edu.ifpe.recife.avalon.model.questao.ComponenteCurricular;
import br.edu.ifpe.recife.avalon.model.questao.MultiplaEscolha;
import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.TipoQuestaoEnum;
import br.edu.ifpe.recife.avalon.model.questao.VerdadeiroFalso;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.servico.ComponenteCurricularServico;
import br.edu.ifpe.recife.avalon.servico.QuestaoServico;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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

        if (TipoQuestaoEnum.MULTIPLA_ESCOLHA.equals(questao.getTipo())) {
            this.alternativas = ((MultiplaEscolha) questao).getAlternativas();
        }else if(TipoQuestaoEnum.VERDADEIRO_FALSO.equals(questao.getTipo())){
            this.respostaVF = ((VerdadeiroFalso) questao).isResposta();
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
     * Método para salvar uma questao de múltipla escolha.
     *
     * @return nav
     */
    private String salvarQuestaoMultiplaEscolha() throws ValidacaoException {
        String navegacao = GO_LISTAR_QUESTAO;

        MultiplaEscolha multiplaEscolha = new MultiplaEscolha();

        copiarQuestao(multiplaEscolha);

        alternativas.get(0).setQuestao(multiplaEscolha);
        alternativas.get(1).setQuestao(multiplaEscolha);
        alternativas.get(2).setQuestao(multiplaEscolha);
        alternativas.get(3).setQuestao(multiplaEscolha);
        alternativas.get(4).setQuestao(multiplaEscolha);

        multiplaEscolha.setAlternativas(alternativas);
        questaoServico.salvar(multiplaEscolha);

        return navegacao;
    }

    private String salvarQuestaoVF() throws ValidacaoException {
        String navegacao = GO_LISTAR_QUESTAO;

        VerdadeiroFalso verdadeiroFalso = new VerdadeiroFalso();

        copiarQuestao(verdadeiroFalso);
        verdadeiroFalso.setResposta(respostaVF);

        questaoServico.salvar(verdadeiroFalso);

        return navegacao;
    }

    private void preencherQuestao() {
        questao.setTipo(tipoSelecionado);
        questao.setCriador(usuarioLogado);
        questao.setDataCriacao(Calendar.getInstance().getTime());
        questao.setComponenteCurricular(buscarComponenteSelecionado());
    }

    private void copiarQuestao(Questao questao) {
        questao.setEnunciado(this.questao.getEnunciado());
        questao.setCriador(this.questao.getCriador());
        questao.setTipo(this.questao.getTipo());
        questao.setDataCriacao(this.questao.getDataCriacao());
        questao.setComponenteCurricular(this.questao.getComponenteCurricular());
        questao.setCompartilhada(this.questao.getCompartilhada());
    }

    /**
     * Método para salvar as alterações realizadas em uma questão.
     *
     * @return nav
     */
    public String salvarEdicao() {
        String navegacao = GO_LISTAR_QUESTAO;

        try {
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
     * Método para excluir uma questão.
     */
    public void excluir() {
        questaoServico.remover(questao);
        questoes.remove(questao);
        questao = null;
    }

    /**
     * Método para retornar a página "Minhas Questões"
     *
     * @return iniciarPagina()
     */
    public String voltarListarQuestoes() {
        return iniciarPagina();
    }

    /**
     * Método para checar se a questão está em modo edição.
     *
     * @return boolean
     */
    public boolean edicaoQuestao() {
        return questao.getId() != null;
    }

    /**
     * Método para salvar um componente curricular.
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
     * Método para buscar todos os componentes curricular cadastrados.
     *
     * @return lista de componentes curricular
     */
    public List<ComponenteCurricular> getTodosComponentesCurricular() {
        return componenteServico.buscarTodosComponentes();
    }

    /**
     * Método para carregar o modal de inserção de componente.
     */
    public void carregarModalComponente() {
        novoComponenteCurricular = new ComponenteCurricular();
        exibirModalComponente = true;
    }

    /**
     * Método para buscar o componente curricular selecionado.
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
     * Método para fechar o modal de componente curricular.
     */
    public void fecharModalComponente() {
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

    public boolean isRespostaVF() {
        return respostaVF;
    }

    public void setRespostaVF(boolean respostaVF) {
        this.respostaVF = respostaVF;
    }

}
