/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean.aluno;

import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.prova.Prova;
import br.edu.ifpe.recife.avalon.model.questao.MultiplaEscolha;
import br.edu.ifpe.recife.avalon.model.questao.VerdadeiroFalso;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.servico.ProvaServico;
import br.edu.ifpe.recife.avalon.servico.QuestaoServico;
import br.edu.ifpe.recife.avalon.util.AvalonUtil;
import java.io.Serializable;
import java.util.ArrayList;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author eduardoamaral
 */
@Named(value = ProvaAlunoBean.NOME)
@SessionScoped
public class ProvaAlunoBean implements Serializable {

    public static final String NOME = "provaAlunoBean";
    private static final String GO_INICIAR_PROVA = "goIniciarProva";
    private static final String GO_LISTAR_PROVAS = "goListarProvas";
    private static final String USUARIO = "usuario";
    private static final String PROVA_QUESTOES_OBRIGATORIAS = "";
    
    @EJB
    private QuestaoServico questaoServico;

    @EJB
    private ProvaServico provaServico;

    private final HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private final Usuario usuarioLogado;

    private Prova prova;
    private List<Prova> provasDisponiveis = new ArrayList<>();
    private List<VerdadeiroFalso> questoesVerdadeiroFalso = new ArrayList<>();
    private List<MultiplaEscolha> questoesMultiplaEscolha = new ArrayList<>();
    private boolean exibirModalResultado;
    private String resultado;

    /**
     * Cria uma nova instância de <code>SimuladoAlunoBean</code>.
     */
    public ProvaAlunoBean() {
        usuarioLogado = (Usuario) sessao.getAttribute(USUARIO);
    }

    /**
     * Método para iniciar a página contendo todas as provas disponíveis.
     *
     * @return rota
     */
    public String iniciarPagina() {
        limparTela();
        bucarProvasDisponiveis();
        return GO_LISTAR_PROVAS;
    }

    /**
     * Método para iniciar uma nova Prova.
     *
     * @param provaSelecionada
     * @return rota
     */
    public String iniciarSimulado(Prova provaSelecionada) {
        limparTelaSimulado();
        prova = provaSelecionada;

        if (!prova.getQuestoes().isEmpty()) {
            if (prova.getQuestoes().get(0) instanceof VerdadeiroFalso) {
                questoesVerdadeiroFalso = (List<VerdadeiroFalso>) (List<?>) questaoServico.buscarQuestoesPorProva(prova.getId());
            } else {
                questoesMultiplaEscolha = (List<MultiplaEscolha>) (List<?>) questaoServico.buscarQuestoesPorProva(prova.getId());
            }

            if (questoesVerdadeiroFalso.isEmpty() && questoesMultiplaEscolha.isEmpty()) {
                exibirMensagemError("Ocorreu um erro ao realizar esta ação.");
                return null;
            }
        }

        return GO_INICIAR_PROVA;
    }

    /**
     * Método para limpar os campos da tela listar simulados.
     */
    private void limparTela() {
        prova = new Prova();
    }

    /**
     * Método para limpar a tela do simulado.
     */
    private void limparTelaSimulado() {
        questoesVerdadeiroFalso.clear();
        questoesMultiplaEscolha.clear();
    }
    
    private void bucarProvasDisponiveis(){
        provasDisponiveis = provaServico.buscarProvasDisponiveis();
    }

    /**
     * Método para finalizar o simulado.
     */
    public void finalizar() {
        double nota;
        
        try {
            if (!questoesVerdadeiroFalso.isEmpty()) {
                verificarTodasQuestoesPreenchidasVF();
            } else {
                verificarTodasQuestoesPreenchidasMS();
            }

            nota = calcularResultado();
            //salvar resultado da prova
        } catch (ValidacaoException ex) {
            exibirMensagemError(ex.getMessage());
        }
    }

    /**
     * Método para verificar se todas as questões V/F foram preenchidas.
     * @throws br.edu.ifpe.recife.avalon.excecao.ValidacaoException
     */
    public void verificarTodasQuestoesPreenchidasVF() throws ValidacaoException {
        for (VerdadeiroFalso questao : questoesVerdadeiroFalso) {
            if (questao.getRespostaUsuario() == null) {
                throw new ValidacaoException(AvalonUtil.getInstance().getMensagemValidacao(PROVA_QUESTOES_OBRIGATORIAS));
            }
        }
    }

    /**
     * Método para verificar se todas as questões de múltipla escolha foram preenchidas.
     * @throws ValidacaoException 
     */
    public void verificarTodasQuestoesPreenchidasMS() throws ValidacaoException {
        for (MultiplaEscolha questao : questoesMultiplaEscolha) {
            if (questao.getRespostaUsuario() == null) {
                throw new ValidacaoException(AvalonUtil.getInstance().getMensagemValidacao(PROVA_QUESTOES_OBRIGATORIAS));
            }
        }
    }

    /**
     * Método para calcular a porcentagem de acerto do aluno no simulado.
     */
    private double calcularResultado() {
        double quantidadeQuestoes;
        double respostasCertas;

        if (!questoesVerdadeiroFalso.isEmpty()) {
            quantidadeQuestoes = questoesVerdadeiroFalso.size();
            respostasCertas = verificarRespostasVF();
        } else {
            quantidadeQuestoes = questoesMultiplaEscolha.size();
            respostasCertas = verificarRespostasMS();
        }
        
        return (respostasCertas / quantidadeQuestoes);
    }

    /**
     * Método para verificar a quantidade de acertos do usúario em questões de
     * V/F.
     *
     * @return quantidade de acertos
     */
    private double verificarRespostasVF() {
        double quantidadeAcertos = 0;

        for (VerdadeiroFalso questao : questoesVerdadeiroFalso) {
            if (questao.getResposta().equals(questao.getRespostaUsuario())) {
                quantidadeAcertos++;
            }
        }

        return quantidadeAcertos;
    }

    /**
     * Método para verificar a quantidade de acertos do usuário em questões de
     * múltipla escolha.
     *
     * @return quantidade de acertos
     */
    private double verificarRespostasMS() {
        double quantidadeAcertos = 0;

        for (MultiplaEscolha questao : questoesMultiplaEscolha) {
            if (questao.getOpcaoCorreta().equals(questao.getRespostaUsuario())) {
                quantidadeAcertos++;
            }
        }

        return quantidadeAcertos;
    }

    /**
     * Método para fechar o modal de resultado.
     *
     * @return navegacao
     */
    public String fecharModalResultado() {
        exibirModalResultado = false;
        return iniciarPagina();
    }

    /**
     * Método para exibir mensagem de erro.
     *
     * @param mensagem - mensagem a ser exibida.
     */
    private void exibirMensagemError(String mensagem) {
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, null);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }
    
    public List<MultiplaEscolha> getQuestoesMultiplaEscolha() {
        return questoesMultiplaEscolha;
    }

    public List<VerdadeiroFalso> getQuestoesVerdadeiroFalso() {
        return questoesVerdadeiroFalso;
    }

    public boolean isExibirModalResultado() {
        return exibirModalResultado;
    }

    public String getResultado() {
        return resultado;
    }

    public Prova getProva() {
        return prova;
    }

    public List<Prova> getProvasDisponiveis() {
        return provasDisponiveis;
    }

}
