/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean.aluno;

import br.edu.ifpe.recife.avalon.model.prova.ProvaAluno;
import br.edu.ifpe.recife.avalon.model.questao.VerdadeiroFalso;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.servico.ProvaServico;
import java.io.Serializable;
import java.util.ArrayList;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

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
    private ProvaServico provaServico;

    private final HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private final Usuario usuarioLogado;

    private List<ProvaAluno> provasResultados = new ArrayList<>();
    private ProvaAluno provaAlunoDetalhe = new ProvaAluno();
    private boolean provaVF;

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
            provaVF = provaAlunoDetalhe.getProva().getQuestoes().get(0) instanceof VerdadeiroFalso;
            return GO_DETALHAR_RESULTADO;
        }

        return null;
    }

    public boolean isProvaVF() {
        return provaVF;
    }

    public List<ProvaAluno> getProvasResultados() {
        return provasResultados;
    }

    public ProvaAluno getProvaAlunoDetalhe() {
        return provaAlunoDetalhe;
    }

}
