/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean;

import br.edu.ifpe.recife.avalon.model.questao.Alternativa;
import br.edu.ifpe.recife.avalon.model.questao.MultiplaEscolha;
import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.TipoQuestaoEnum;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.servico.QuestaoServico;
import br.edu.ifpe.recife.avalon.util.AvalonUtil;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 *
 * @author eduardo.f.amaral
 */
@Named(value = "questaoBean")
@SessionScoped
public class QuestaoBean implements Serializable {

    private static final String MSG_QUESTAO_UNICA = "questao.enunciado.repetido";
    private static final String GO_LISTAR_QUESTAO = "goListarQuestao";
    private static final String GO_ADD_QUESTAO = "goAddQuestao";
    private static final String GO_ALTERAR_QUESTAO = "goAlterarQuestao";
    private static final String ALTERNATIVAS_IGUAIS = "questao.alternativas.iguais";

    private List<Alternativa> alternativas = new ArrayList<Alternativa>();

    private Alternativa alt1 = new Alternativa();
    private Alternativa alt2 = new Alternativa();
    private Alternativa alt3 = new Alternativa();
    private Alternativa alt4 = new Alternativa();
    private Alternativa alt5 = new Alternativa();

    @EJB
    private QuestaoServico questaoServico;

    private List<TipoQuestaoEnum> tipoQuestoes = new ArrayList<>();

    private TipoQuestaoEnum tipoSelecionado = TipoQuestaoEnum.DISCURSIVA;

    private List<Questao> questoes = new ArrayList<>();

    @Valid
    private Questao novaQuestao = new Questao();

    private Questao questaoSelecionada;

    private boolean exibirModalConfirmarExclusao = false;

    private Usuario usuarioLogado;

    HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

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
        this.limparTela();
        return GO_ADD_QUESTAO;
    }

    /**
     * Método para inicializar as variáveis da tela de alteração da questão.
     *
     * @param questaoSelecionada
     * @return goAlterarQuestao
     */
    public String iniciparPaginaAlteracao(Questao questaoSelecionada) {
        this.questaoSelecionada = questaoSelecionada;

        if (TipoQuestaoEnum.MULTIPLA_ESCOLHA.equals(questaoSelecionada.getTipo())) {
            this.alternativas = ((MultiplaEscolha) questaoSelecionada).getAlternativas();
        }

        return GO_ALTERAR_QUESTAO;
    }

    public QuestaoBean() {
        this.usuarioLogado = (Usuario) sessao.getAttribute("usuario");
        this.novaQuestao = new Questao();
        this.carregarTiposQuestao();

        this.alternativas = new ArrayList<>();
        alternativas.add(alt1);
        alternativas.add(alt2);
        alternativas.add(alt3);
        alternativas.add(alt4);
        alternativas.add(alt5);
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
        novaQuestao.setTipo(tipoSelecionado);
        novaQuestao.setCriador(usuarioLogado);
        novaQuestao.setDataCriacao(Calendar.getInstance().getTime());

        if (questaoServico.isEnunciadoPorTipoValido(novaQuestao)) {
            if (TipoQuestaoEnum.MULTIPLA_ESCOLHA.equals(tipoSelecionado)) {
                return salvarQuestaoMultiplaEscolha();
            } else {
                questaoServico.salvar(novaQuestao);
            }
            limparTela();
            buscarQuestoes();
        } else {
            exibirMensagemEnunciadoInvalido();
            return "";
        }

        return GO_LISTAR_QUESTAO;
    }

    /**
     * Método para salvar uma questao de múltipla escolha.
     *
     * @return nav
     */
    private String salvarQuestaoMultiplaEscolha() {
        if (questaoServico.isAlternativasValidas(alternativas)) {
            MultiplaEscolha questaoMultipla = new MultiplaEscolha();

            questaoMultipla.setEnunciado(novaQuestao.getEnunciado());
            questaoMultipla.setCriador(novaQuestao.getCriador());
            questaoMultipla.setTipo(TipoQuestaoEnum.MULTIPLA_ESCOLHA);
            questaoMultipla.setDataCriacao(novaQuestao.getDataCriacao());

            alternativas.get(0).setQuestao(questaoMultipla);
            alternativas.get(1).setQuestao(questaoMultipla);
            alternativas.get(2).setQuestao(questaoMultipla);
            alternativas.get(3).setQuestao(questaoMultipla);
            alternativas.get(4).setQuestao(questaoMultipla);

            questaoMultipla.setAlternativas(alternativas);
            questaoServico.salvar(questaoMultipla);
            
            limparTela();
            buscarQuestoes();

            return GO_LISTAR_QUESTAO;
        } else {
            exibirMensagemAlternativasInvalidas();
            return "";
        }
    }

    /**
     * Método para salvar as alterações realizadas em uma questão.
     *
     * @return nav
     */
    public String salvarEdicao() {

        if (questaoServico.isEdicaoEnunciadoPorTipoValido(questaoSelecionada)) {
            if (TipoQuestaoEnum.MULTIPLA_ESCOLHA.equals(questaoSelecionada.getTipo())) {
                if (!questaoServico.isAlternativasValidas(alternativas)) {
                    exibirMensagemAlternativasInvalidas();
                    return "";
                }
            }
            questaoServico.alterar(questaoSelecionada);
            limparTela();
            buscarQuestoes();
        } else {
            exibirMensagemEnunciadoInvalido();
            return "";
        }

        return GO_LISTAR_QUESTAO;
    }

    /**
     * Método para exibição de mensagem de validação de alternativas.
     */
    private void exibirMensagemAlternativasInvalidas() {
        FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_ERROR, AvalonUtil.getInstance().getMensagemValidacao(ALTERNATIVAS_IGUAIS), null);
        FacesContext.getCurrentInstance().addMessage(null, mensagem);
    }

    /**
     * Método para exibição de mensagem de validação do enunciado.
     */
    private void exibirMensagemEnunciadoInvalido() {
        FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_ERROR, AvalonUtil.getInstance().getMensagemValidacao(MSG_QUESTAO_UNICA), null);
        FacesContext.getCurrentInstance().addMessage(null, mensagem);
    }

    /**
     * Método para limpar os campos da tela.
     */
    private void limparTela() {
        tipoSelecionado = TipoQuestaoEnum.DISCURSIVA;
        novaQuestao = new Questao();

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
     * @param questao
     */
    public void selecionarQuestao(Questao questao) {
        questaoSelecionada = questao;
    }

    /**
     * Método para excluir uma questão
     */
    public void excluir() {
        questaoServico.remover(questaoSelecionada);
        questoes.remove(questaoSelecionada);
        questaoSelecionada = null;
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

    public Questao getNovaQuestao() {
        return novaQuestao;
    }

    public void setNovaQuestao(Questao novaQuestao) {
        this.novaQuestao = novaQuestao;
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

    public Questao getQuestaoSelecionada() {
        return questaoSelecionada;
    }

    public void setQuestaoSelecionada(Questao questaoSelecionada) {
        this.questaoSelecionada = questaoSelecionada;
    }

}
