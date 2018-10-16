/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean.professor;

import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.prova.Prova;
import br.edu.ifpe.recife.avalon.model.prova.ProvaAluno;
import br.edu.ifpe.recife.avalon.model.questao.MultiplaEscolha;
import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.VerdadeiroFalso;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.servico.ComponenteCurricularServico;
import br.edu.ifpe.recife.avalon.servico.ProvaServico;
import br.edu.ifpe.recife.avalon.servico.QuestaoServico;
import br.edu.ifpe.recife.avalon.util.AvalonUtil;
import br.edu.ifpe.recife.avalon.viewhelper.ComponenteCurricularViewHelper;
import br.edu.ifpe.recife.avalon.viewhelper.PdfGeneratorViewHelper;
import br.edu.ifpe.recife.avalon.viewhelper.QuestaoDetalhesViewHelper;
import br.edu.ifpe.recife.avalon.viewhelper.VisualizarAvaliacaoViewHelper;
import java.io.IOException;
import java.io.OutputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author eduardoamaral
 */
@Named(value = ProvaBean.NOME)
@SessionScoped
public class ProvaBean extends AvaliacaoBean {

    public static final String NOME = "provaBean";
    private static final String GO_GERAR_PROVA = "goGerarProva";
    private static final String GO_LISTAR_PROVA = "goListarProva";
    private static final String GO_IMPRIMIR_PROVA = "goImprimirProva";
    private static final String GO_DETALHAR_PROVA = "goDetalharProva";
    private static final String GO_RESULTADOS_PROVA = "goResultadosProva";
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
    private final HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    private Usuario usuarioLogado;
    private List<Prova> provas;
    private boolean exibirModalExclusao;
    private Prova prova;

    private List<ProvaAluno> resultados;
    private ProvaAluno provaAlunoDetalhe;
    private Prova provaResultadoSelecionada;
    private boolean exibirModalReagendamento;
    private Date dataHoraInicioReagendamento;
    private Date dataHoraFimReagendamento;

    /**
     * Cria uma nova instância de <code>ProvaBean</code>.
     */
    public ProvaBean() {
        usuarioLogado = (Usuario) sessao.getAttribute(USUARIO);
        componenteViewHelper = new ComponenteCurricularViewHelper();
        detalhesViewHelper = new QuestaoDetalhesViewHelper();
        visualizarViewHelper = new VisualizarAvaliacaoViewHelper();
        prova = new Prova();
        provas = new ArrayList<>();
    }

    /**
     * Inicializa a página de listar provas.
     *
     * @return
     */
    public String iniciarPagina() {
        usuarioLogado = (Usuario) sessao.getAttribute(USUARIO);
        limparPagina();
        buscarProvas();

        return GO_LISTAR_PROVA;
    }

    /**
     * Inicializa a página de gerar nova prova.
     *
     * @return
     */
    public String iniciarPaginaGerar() {
        inicializarHelpers(true);
        limparPaginaGerar();

        return GO_GERAR_PROVA;
    }

    /**
     * Inicializa a página de detalhar prova.
     *
     * @param provaSelecionada
     * @return
     */
    public String iniciarPaginaDetalhar(Prova provaSelecionada) {
        prova = provaSelecionada;
        carregarQuestoesDetalhar();
        return GO_DETALHAR_PROVA;
    }

    /**
     * Inicializa a página para gerarArquivo uma prova.
     *
     * @return rota para página de geração de prova
     */
    public String iniciarPaginaImprimir() {
        inicializarHelpers(false);
        limparPaginaImprimir();

        return GO_IMPRIMIR_PROVA;
    }

    /**
     * Inicializa a página de resultados de uma prova.
     *
     * @param prova
     * @return
     */
    public String iniciarPaginaResultados(Prova prova) {
        provaResultadoSelecionada = prova;
        buscarResultados(prova);

        return GO_RESULTADOS_PROVA;
    }

    /**
     * Carrega as provas do professor logado.
     */
    private void buscarProvas() {
        this.provas.clear();
        this.provas = provaServico.buscarProvasPorProfessor(usuarioLogado.getEmail());
    }


    /**
     * Recupera todos os resultados dos alunos em uma prova.
     *
     * @param prova
     */
    private void buscarResultados(Prova prova) {
        resultados = provaServico.buscarResultadosProva(prova);
    }

    /**
     * Limpa a página Listar Provas.
     */
    private void limparPagina() {
        prova = new Prova();
        fecharModalExclusao();
        fecharModalReagendamento();
    }

    /**
     * Limpra a página de geração de provas.
     */
    private void limparPaginaGerar() {
        prova = new Prova();
        inicializarQuestoes();
    }

    /**
     * Limpa os campos da tela de geração de provas.
     */
    private void limparPaginaImprimir() {
        inicializarQuestoes();
    }

    

    /**
     * Inicializa os ViewHelpers.
     *
     * @param apenasQuestoesObjetivas
     */
    private void inicializarHelpers(boolean apenasQuestoesObjetivas) {
        componenteViewHelper.inicializar(componenteServico);
        detalhesViewHelper.inicializar();
        getPesquisarQuestoesViewHelper().inicializar(questaoServico, usuarioLogado, apenasQuestoesObjetivas);
    }

    /**
     * Gera uma prova a partir das questões selecionada.
     */
    public void imprimirProva() {
        if (getQuestoesSelecionadas().isEmpty()) {
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
     *
     * @param provaSelecionada - prova selecionada.
     */
    public void selecionarProvaExclusao(Prova provaSelecionada) {
        prova = provaSelecionada;
        exibirModalExclusao();
    }

    /**
     * Exclui uma prova selecionada.
     */
    public void excluirProva() {
        provaServico.excluir(prova);
        provas.remove(prova);
    }

    /**
     * Exibe o modal de exclusão.
     */
    private void exibirModalExclusao() {
        exibirModalExclusao = true;
    }

    /**
     * Fecha o modal de exclusão.
     */
    public void fecharModalExclusao() {
        exibirModalExclusao = false;
    }

    /**
     * Salva uma nova prova.
     *
     * @return
     */
    public String adicionarProva() {
        String navegacao = null;

        try {
            prova.setComponenteCurricular(componenteViewHelper.getComponenteCurricularPorId(getPesquisarQuestoesViewHelper().getFiltro().getIdComponenteCurricular()));
            prova.setDataCriacao(Calendar.getInstance().getTime());
            prova.setProfessor(usuarioLogado);
            prova.setQuestoes(new ArrayList<Questao>());
            prova.getQuestoes().addAll(getQuestoesSelecionadas());
            provaServico.salvar(prova);
            navegacao = iniciarPagina();
        } catch (ValidacaoException ex) {
            exibirMensagem(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }

        return navegacao;
    }

    /**
     * Inicializa a lista de questões a detalhar.
     */
    private void carregarQuestoesDetalhar() {

        if (!prova.getQuestoes().isEmpty()) {
            try {

                if (prova.getQuestoes().get(0) instanceof VerdadeiroFalso) {
                    visualizarViewHelper.setQuestoesVerdadeiroFalso((List<VerdadeiroFalso>) (List<?>) prova.getQuestoes());
                } else {
                    visualizarViewHelper.setQuestoesMultiplaEscolha((List<MultiplaEscolha>) (List<?>) prova.getQuestoes());
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
     * Seleciona uma prova para reagendamento.
     *
     * @param provaSelecionada
     */
    public void selecionarProvaReagendamento(Prova provaSelecionada) {
        prova = provaSelecionada;
        dataHoraInicioReagendamento = new Date(prova.getDataHoraInicio().getTime());
        dataHoraFimReagendamento = new Date(prova.getDataHoraFim().getTime());
        exibirModalReagendamento();
    }

    /**
     * Reagenda uma prova.
     */
    public void reagendarProva() {
        try {
            validarReagendamento();
            prova.setDataHoraInicio(dataHoraInicioReagendamento);
            prova.setDataHoraFim(dataHoraFimReagendamento);
            provaServico.reagendarProva(prova);
            fecharModalReagendamento();
        } catch (ValidacaoException ex) {
            exibirMensagem(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }
    }

    /**
     * Exibe o modal de reagendamento.
     */
    public void exibirModalReagendamento() {
        exibirModalReagendamento = true;
    }

    /**
     * Fecha o modal de reagendamento.
     */
    public void fecharModalReagendamento() {
        exibirModalReagendamento = false;
        buscarProvas();
    }

    /**
     * Valida se a data de início de uma prova em andamento foi alterada.
     *
     * @throws ValidacaoException
     */
    private void validarReagendamento() throws ValidacaoException {
        if (prova.getDataHoraInicio().getTime() != dataHoraInicioReagendamento.getTime() && prova.getDataHoraInicio().before(Calendar.getInstance().getTime())) {
            throw new ValidacaoException(AvalonUtil.getInstance().getMensagemValidacao("prova.reagendamento.data.inicio.em.andamento"));
        }
    }

    public String imprimirPdf() {
        try {
            PdfGeneratorViewHelper pdf = new PdfGeneratorViewHelper();
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
            
            response.reset();   // Algum filtro pode ter configurado alguns cabeçalhos no buffer de antemão. Queremos livrar-se deles, senão ele pode colidir.
            response.setHeader("Content-Type", "application/pdf");  // Define apenas o tipo de conteúdo, Utilize se necessário ServletContext#getMimeType() para detecção automática com base em nome de arquivo.
            response.setHeader("Content-Disposition", "attachment;"+"filename=Prova_"+System.currentTimeMillis()+".pdf");
            
            OutputStream responseOutputStream = FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream();
            List<Questao> questoesImpressao = new ArrayList<>();
            questoesImpressao.addAll(getQuestoesSelecionadas());
            
            pdf.gerarArquivo(responseOutputStream, questoesImpressao);
            
            facesContext.responseComplete();
        } catch (IOException ex) {
            Logger.getLogger(ProvaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "";
    }

    /*
        GETTERS AND SETTERS
     */
    public ComponenteCurricularViewHelper getComponenteViewHelper() {
        return componenteViewHelper;
    }

    public QuestaoDetalhesViewHelper getDetalhesViewHelper() {
        return detalhesViewHelper;
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

    public List<ProvaAluno> getResultados() {
        return resultados;
    }

    public Prova getProvaResultadoSelecionada() {
        return provaResultadoSelecionada;
    }

    public ProvaAluno getProvaAlunoDetalhe() {
        return provaAlunoDetalhe;
    }

    public boolean isExibirModalReagendamento() {
        return exibirModalReagendamento;
    }

    public Date getDataHoraInicioReagendamento() {
        return dataHoraInicioReagendamento;
    }

    public void setDataHoraInicioReagendamento(Date dataHoraInicioReagendamento) {
        this.dataHoraInicioReagendamento = dataHoraInicioReagendamento;
    }

    public Date getDataHoraFimReagendamento() {
        return dataHoraFimReagendamento;
    }

    public void setDataHoraFimReagendamento(Date dataHoraFimReagendamento) {
        this.dataHoraFimReagendamento = dataHoraFimReagendamento;
    }

}
