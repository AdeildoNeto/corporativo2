/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean.aluno;

import br.edu.ifpe.recife.avalon.model.prova.ProvaAluno;
import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.VerdadeiroFalso;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.servico.ProvaServico;
import br.edu.ifpe.recife.avalon.servico.QuestaoServico;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpSession;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author eduardoamaral
 */
@Named(value = ResultadoAlunoBean.NOME)
@SessionScoped
public class ResultadoAlunoBean implements Serializable {

    public static final String NOME = "resultadoAlunoBean";
    private static final String USUARIO = "usuario";
    private static final String GO_LISTAR_RESULTADO = "goListarResultados";
    private static final String GO_DETALHAR_RESULTADO = "goDetalharResultado";

    @EJB
    private QuestaoServico questaoServico;

    @EJB
    private ProvaServico provaServico;

    private final HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private final Usuario usuarioLogado;

    private List<ProvaAluno> provasResultados = new ArrayList<>();
    private ProvaAluno provaAlunoDetalhe = new ProvaAluno();
    private boolean quetoesVF;

    /**
     * Cria uma nova instância de <code>ResultadoAlunoBean</code>.
     */
    public ResultadoAlunoBean() {
        usuarioLogado = (Usuario) sessao.getAttribute(USUARIO);
    }

    /**
     * Inicia a página contendo todas as provas realizadas pelo aluno e seus
     * respectivos resultados.
     *
     * @return rota
     */
    public String iniciarPagina() {
        buscarProvasResultados();
        return GO_LISTAR_RESULTADO;
    }

    /**
     * Busca por todos as provas realizadas pelo aluon e seus respectivos
     * resultados.
     */
    private void buscarProvasResultados() {
        provasResultados = provaServico.buscarResultadosProvasAluno(usuarioLogado);
    }

    /**
     * Inicializa a prova selecionada para detalha-la.
     *
     * @param provaSelecionada
     * @return rota
     */
    public String iniciarPaginaDetalhar(ProvaAluno provaSelecionada) {
        provaAlunoDetalhe = provaSelecionada;

        if (!provaAlunoDetalhe.getProva().getQuestoes().isEmpty()) {
            if (provaAlunoDetalhe.getProva().getQuestoes().get(0) instanceof VerdadeiroFalso) {
                quetoesVF = true;
            } else {
                quetoesVF = false;
            }

            return GO_DETALHAR_RESULTADO;

        } else {
            exibirMensagemError("Ocorreu um erro ao realizar esta ação.");
        }

        return null;
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

    /**
     * Recupera a imagem de uma questão.
     *
     * @return
     * @throws IOException
     */
    public StreamedContent getImagem() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        } else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
            String questaoId = context.getExternalContext().getRequestParameterMap().get("questaoId");
            Questao questao = questaoServico.buscarQuestaoPorId(Long.valueOf(questaoId));
            return new DefaultStreamedContent(new ByteArrayInputStream(questao.getImagem().getArquivo()));
        }
    }

    public boolean isQuetoesVF() {
        return quetoesVF;
    }

    public List<ProvaAluno> getProvasResultados() {
        return provasResultados;
    }

    public ProvaAluno getProvaAlunoDetalhe() {
        return provaAlunoDetalhe;
    }

}
