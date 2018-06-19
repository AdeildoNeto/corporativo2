/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean.professor;

import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.prova.Prova;
import br.edu.ifpe.recife.avalon.model.questao.MultiplaEscolha;
import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.VerdadeiroFalso;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.servico.ComponenteCurricularServico;
import br.edu.ifpe.recife.avalon.servico.ProvaServico;
import br.edu.ifpe.recife.avalon.servico.QuestaoServico;
import br.edu.ifpe.recife.avalon.util.AvalonUtil;
import br.edu.ifpe.recife.avalon.viewhelper.ComponenteCurricularViewHelper;
import br.edu.ifpe.recife.avalon.viewhelper.PesquisarQuestaoViewHelper;
import br.edu.ifpe.recife.avalon.viewhelper.QuestaoDetalhesViewHelper;
import br.edu.ifpe.recife.avalon.viewhelper.VisualizarAvaliacaoViewHelper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author eduardoamaral
 */
@Named(value = ProvaBean.NOME)
@SessionScoped
public class ProvaBean implements Serializable {

    public static final String NOME = "provaBean";
    private static final String GO_GERAR_PROVA = "goGerarProva";
    private static final String GO_LISTAR_PROVA = "goListarProva";
    private static final String GO_IMPRIMIR_PROVA = "goImprimirProva";
    private static final String GO_DETALHAR_PROVA = "goDetalharProva";
    private static final String USUARIO = "usuario";

    @EJB
    private QuestaoServico questaoServico;

    @EJB
    private ComponenteCurricularServico componenteServico;
    
    @EJB
    private ProvaServico provaServico;

    private final VisualizarAvaliacaoViewHelper visualizarViewHelper;
    private final ComponenteCurricularViewHelper componenteViewHelper;
    private final QuestaoDetalhesViewHelper detalhesViewHelper;
    private final PesquisarQuestaoViewHelper pesquisarQuestoesViewHelper;
    private final HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    private Usuario usuarioLogado;
    private List<Questao> questoes;
    private List<Prova> provas;
    private Set<Questao> questoesSelecionadas;
    private boolean todosSelecionados;
    private boolean exibirModalExclusao;
    private boolean exibirModalTitulo;
    private Prova prova;
    private String titulo;
    private String headerModalTitulo;
   
    /**
     * Cria uma nova instância de <code>ProvaBean</code>.
     */
    public ProvaBean() {
        usuarioLogado = (Usuario) sessao.getAttribute(USUARIO);
        componenteViewHelper = new ComponenteCurricularViewHelper();
        detalhesViewHelper = new QuestaoDetalhesViewHelper();
        pesquisarQuestoesViewHelper = new PesquisarQuestaoViewHelper();
        visualizarViewHelper = new VisualizarAvaliacaoViewHelper();
        questoes = new ArrayList<>();
        questoesSelecionadas = new HashSet<>();
        prova = new Prova();
        provas = new ArrayList<>();
        headerModalTitulo = AvalonUtil.getInstance().getMensagem("prova.nova");
    }
    
    public String iniciarPagina(){
        usuarioLogado = (Usuario) sessao.getAttribute(USUARIO);
        limparPagina();
        buscarProvas();
        
        return GO_LISTAR_PROVA;
    }
    
    public String iniciarPaginaGerar(){
        inicializarHelpers(true);
        limparPaginaGerar();
        prova.setTitulo(titulo);
        
        return GO_GERAR_PROVA;
    }
    
    public String iniciarPaginaDetalhar(Prova provaSelecionada){
        prova = provaSelecionada;
        carregarQuestoesDetalhar();
        return GO_DETALHAR_PROVA;
    }
    
    /**
     * Inicializa a página para imprimir uma prova.
     * 
     * @return rota para página de geração de prova
     */
    public String iniciarPaginaImprimir() {
        inicializarHelpers(false);
        limparPaginaImprimir();

        return GO_IMPRIMIR_PROVA;
    }
    
    /**
     * Carrega as provas do professor logado.
     */
    private void buscarProvas(){
        this.provas.clear();
        this.provas = provaServico.buscarProvasPorProfessor(usuarioLogado.getEmail());
    }

    /**
     * Carrega as as questões do a partir do filtro informado.
     */
    private void buscarQuestoes() {
        this.questoes.clear();
        this.questoesSelecionadas.clear();
        this.todosSelecionados = false;
        this.questoes = pesquisarQuestoesViewHelper.pesquisar();
    }
    
    private void limparPagina(){
        prova = new Prova();
        fecharModalExclusao();
        fecharModalTitulo();
    }

    private void limparPaginaGerar(){
        prova = new Prova();
        inicializarQuestoes();
    }
    
    /**
     * Limpa os campos da tela de geração de provas.
     */
    private void limparPaginaImprimir() {
        inicializarQuestoes();
    }
    
    private void inicializarQuestoes(){
        todosSelecionados = false;
        questoesSelecionadas.clear();
        questoes.clear();
    }
    
    private void inicializarHelpers(boolean apenasQuestoesObjetivas){
        componenteViewHelper.inicializar(componenteServico);
        detalhesViewHelper.inicializar();
        pesquisarQuestoesViewHelper.inicializar(questaoServico, usuarioLogado, apenasQuestoesObjetivas);
    }

    /**
     * Seleciona uma questão da lista de questões.
     * 
     * @param questao - questão selecionada.
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
     * Pesquisa as questões disponíveis para impressão.
     */
    public void pesquisar() {
        buscarQuestoes();
        if (questoes.isEmpty()) {
            exibirMensagem(FacesMessage.SEVERITY_INFO, AvalonUtil.getInstance().getMensagem("pesquisa.sem.dados"));
        }
    }

    /**
     * Exibe mensagem na tela.
     */
     private void exibirMensagem(FacesMessage.Severity severity, String mensagem) {
        FacesMessage facesMessage = new FacesMessage(severity, mensagem, null);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }

    /**
     * Marca ou desmarca todas as questões disponíveis para impressão.
     */
    public void selecionarTodos() {
        questoesSelecionadas = new HashSet<>();

        for (Questao questao : questoes) {
            questao.setSelecionada(todosSelecionados);
            selecionarQuestao(questao);
        }

    }

    /**
     * Gera uma prova a partir das questões selecionada.
     */
    public void imprimirProva() {
        if (questoesSelecionadas.isEmpty()) {
            exibirMensagem(FacesMessage.SEVERITY_ERROR, AvalonUtil.getInstance().getMensagemValidacao("selecione.uma.questao"));
        } else {
            RequestContext.getCurrentInstance().execute(montarUrlProva());
        }
    }

    /**
     * Monta a URL da página de impressão da prova.
     * 
     * @return url da prova.
     */
    private String montarUrlProva() {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        StringBuilder builder = new StringBuilder();
        builder.append("window.open('");
        builder.append(path);
        builder.append("/professor/prova/impressao.xhtml");
        builder.append("')");
        
        return builder.toString();
    }
    
    /**
     * Seleciona uma prova para exclusão.
     * @param provaSelecionada - prova selecionada.
     */
    public void selecionarProvaExclusao(Prova provaSelecionada){
        prova = provaSelecionada;
        exibirModalExclusao();
    }
    
    /**
     * Exclui uma prova selecionada.
     */
    public void excluirProva(){
        provaServico.excluir(prova);
        provas.remove(prova);
    }
    
    /**
     * Exibe o modal de exclusão.
     */
    private void exibirModalExclusao(){
        exibirModalExclusao = true;
    }
    
    /**
     * Fecha o modal de exclusão.
     */
    public void fecharModalExclusao(){
        exibirModalExclusao = false;
    }
    
    public void exibirModalTitulo(){
        titulo = null;
        exibirModalTitulo = true;
    }
    
    public void fecharModalTitulo(){
        exibirModalTitulo = false;
    }
    
    public String adicionarProva(){
        String navegacao = null;

        try {
            prova.setComponenteCurricular(componenteViewHelper.getComponenteCurricularPorId(pesquisarQuestoesViewHelper.getFiltro().getIdComponenteCurricular()));
            prova.setDataCriacao(Calendar.getInstance().getTime());
            prova.setProfessor(usuarioLogado);
            prova.setQuestoes(new ArrayList<Questao>());
            prova.getQuestoes().addAll(questoesSelecionadas);
            provaServico.salvar(prova);
            navegacao = iniciarPagina();
        } catch (ValidacaoException ex) {
            exibirMensagem(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }

        return navegacao;
    }
    
    private void carregarQuestoesDetalhar(){
        visualizarViewHelper.inicializar(questaoServico);
        
        if (!prova.getQuestoes().isEmpty()) {
            try {

                if (prova.getQuestoes().get(0) instanceof VerdadeiroFalso) {
                    visualizarViewHelper.setQuestoesVerdadeiroFalso((List<VerdadeiroFalso>) (List<?>) questaoServico.buscarQuestoesPorProva(prova.getId()));
                } else {
                    visualizarViewHelper.setQuestoesMultiplaEscolha((List<MultiplaEscolha>) (List<?>) questaoServico.buscarQuestoesPorProva(prova.getId()));
                }

                if (visualizarViewHelper.getQuestoesMultiplaEscolha().isEmpty() && visualizarViewHelper.getQuestoesVerdadeiroFalso().isEmpty()) {
                    throw new ValidacaoException(AvalonUtil.getInstance().getMensagem("prova.vazia"));
                }

            } catch (ValidacaoException ex) {
                exibirMensagem(FacesMessage.SEVERITY_ERROR, ex.getMessage());
            }
        }
    }
    
  
    /**
     * Recupera arquivo para exibicao
     * @return arquivo
     * @throws IOException
     */

    
    public StreamedContent getImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return new DefaultStreamedContent();
        }
        else {
            String questaoId = context.getExternalContext().getRequestParameterMap().get("questaoId");
            Questao questao = questaoServico.buscarQuestaoPorId(Long.valueOf(questaoId));
            DefaultStreamedContent arquivo = new DefaultStreamedContent(new ByteArrayInputStream(questao.getImagem().getArquivo()));
            return arquivo;
        }
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

    public PesquisarQuestaoViewHelper getPesquisarQuestoesViewHelper() {
        return pesquisarQuestoesViewHelper;
    }

    public VisualizarAvaliacaoViewHelper getVisualizarViewHelper() {
        return visualizarViewHelper;
    }
    
    public Prova getProva() {
        return prova;
    }

    public List<Prova> getProvas() {
        return provas;
    }

    public boolean isExibirModalExclusao() {
        return exibirModalExclusao;
    }

    public boolean isExibirModalTitulo() {
        return exibirModalTitulo;
    }

    public String getHeaderModalTitulo() {
        return headerModalTitulo;
    }
    
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
        
}
