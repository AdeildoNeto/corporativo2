/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean.professor;

import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.servico.ComponenteCurricularServico;
import br.edu.ifpe.recife.avalon.servico.QuestaoServico;
import br.edu.ifpe.recife.avalon.util.AvalonUtil;
import br.edu.ifpe.recife.avalon.viewhelper.ComponenteCurricularViewHelper;
import br.edu.ifpe.recife.avalon.viewhelper.PesquisarQuestaoViewHelper;
import br.edu.ifpe.recife.avalon.viewhelper.QuestaoDetalhesViewHelper;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author eduardoamaral
 */
@Named(value = ProvaBean.NOME)
@SessionScoped
public class ProvaBean implements Serializable {

    public static final String NOME = "provaBean";
    private static final String GO_GERAR_PROVA = "goGerarProva";
    private static final String USUARIO = "usuario";

    @EJB
    private QuestaoServico questaoServico;

    @EJB
    private ComponenteCurricularServico componenteServico;

    private final ComponenteCurricularViewHelper componenteViewHelper;
    private final QuestaoDetalhesViewHelper detalhesViewHelper;
    private final PesquisarQuestaoViewHelper pesquisarViewHelper;
    private final HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    private Usuario usuarioLogado;
    private List<Questao> questoes;
    private Set<Questao> questoesSelecionadas;
    private boolean todosSelecionados;

    /**
     * Método para iniciar a página de geração de prova.
     *
     * @return rota para página de geração de prova
     */
    public String iniciarPagina() {
        usuarioLogado = (Usuario) sessao.getAttribute(USUARIO);
        componenteViewHelper.inicializar(componenteServico);
        detalhesViewHelper.inicializar();
        pesquisarViewHelper.inicializar(questaoServico, usuarioLogado, false);
        limparTela();

        return GO_GERAR_PROVA;
    }

    public ProvaBean() {
        usuarioLogado = (Usuario) sessao.getAttribute(USUARIO);
        componenteViewHelper = new ComponenteCurricularViewHelper();
        detalhesViewHelper = new QuestaoDetalhesViewHelper();
        pesquisarViewHelper = new PesquisarQuestaoViewHelper();
        questoes = new ArrayList<>();
        questoesSelecionadas = new HashSet<>();
    }

    /**
     * Método para carregar as questões do usuário.
     */
    private void buscarQuestoes() {
        this.questoes.clear();
        this.questoesSelecionadas.clear();
        this.todosSelecionados = false;
        this.questoes = pesquisarViewHelper.pesquisar();
    }

    /**
     * Método para limpar os campos da tela.
     */
    private void limparTela() {
        todosSelecionados = false;
        questoesSelecionadas.clear();
    }

    /**
     * Método para selecionar uma questão da lista de questões.
     *
     * @param questao
     */
    public void selecionarQuestao(Questao questao) {
        if (questao.isSelecionada()) {
            questoesSelecionadas.add(questao);
        } else {
            questoesSelecionadas.remove(questao);
            todosSelecionados = false;
        }
    }

    /**
     * Método para atualizar a lista de questões disponíveis para impressão.
     */
    public void pesquisar() {
        buscarQuestoes();
        if (questoes.isEmpty()) {
            exibirMensagemPesquisaSemDados();
        }
    }

    private void exibirMensagemPesquisaSemDados() {
        FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_INFO, AvalonUtil.getInstance().getMensagem("pesquisa.sem.dados"), null);
        FacesContext.getCurrentInstance().addMessage(null, mensagem);
    }

    /**
     * Método para selecionar todas as questões disponíveis.
     */
    public void selecionarTodos() {
        questoesSelecionadas = new HashSet<>();

        for (Questao questao : questoes) {
            questao.setSelecionada(todosSelecionados);
            selecionarQuestao(questao);
        }

    }

    /**
     * Método para gerar uma prova a partir das questões selecionada.
     */
    public void imprimirProva() {
        if (questoesSelecionadas.isEmpty()) {
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_ERROR, AvalonUtil.getInstance().getMensagemValidacao("selecione.uma.questao"), null);
            FacesContext.getCurrentInstance().addMessage(null, mensagem);
        } else {
            RequestContext.getCurrentInstance().execute(montarUrlProva());
        }
    }

    private String montarUrlProva() {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        StringBuilder builder = new StringBuilder();
        builder.append("window.open('");
        builder.append(path);
        builder.append("/professor/prova/impressao.xhtml");
        builder.append("')");
        
        return builder.toString();
    }

    /*
        GETTERS AND SETTERS
     */
    public List<Questao> getQuestoes() {
        return questoes;
    }

    public boolean isTodosSelecionados() {
        return todosSelecionados;
    }

    public void setTodosSelecionados(boolean todosSelecionados) {
        this.todosSelecionados = todosSelecionados;
    }

    public Set<Questao> getQuestoesSelecionadas() {
        return questoesSelecionadas;
    }

    public ComponenteCurricularViewHelper getComponenteViewHelper() {
        return componenteViewHelper;
    }

    public QuestaoDetalhesViewHelper getDetalhesViewHelper() {
        return detalhesViewHelper;
    }

    public PesquisarQuestaoViewHelper getPesquisarViewHelper() {
        return pesquisarViewHelper;
    }

}
