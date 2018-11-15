/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean.professor;

import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.turma.Turma;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.servico.TurmaServico;
import br.edu.ifpe.recife.avalon.servico.UsuarioServico;
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
@Named(value = TurmaBean.NOME)
@SessionScoped
public class TurmaBean implements Serializable {

    public static final String NOME = "turmaBean";
    private static final String GO_CADASTRAR_TURMA = "goCadastrarTurma";
    private static final String GO_MINHAS_TURMAS = "goMinhasTurmas";
    private static final String GO_DETALHAR_TURMA = "goDetalharTurma";
    private static final String USUARIO = "usuario";

    @EJB
    private UsuarioServico usuarioServico;
    
    @EJB
    private TurmaServico turmaServico;

    HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    private Usuario usuarioLogado;
    private Turma turma = new Turma();
    private List<Turma> turmas = new ArrayList<>();
    private List<Usuario> alunos = new ArrayList<>();
    private List<Usuario> alunosSelecionados = new ArrayList<>();
    
    private boolean exibirModalExcluir;
    private boolean todosSelecionados = false;

    /**
     * Cria uma nova instância de <code>TurmaBean</code>.
     */
    public TurmaBean() {
        usuarioLogado = (Usuario) sessao.getAttribute(USUARIO);
    }

    /**
     * Inicializa a página minhas turmas.
     *
     * @return
     */
    public String iniciarPagina() {
        usuarioLogado = (Usuario) sessao.getAttribute(USUARIO);
        buscarTurmas();

        return GO_MINHAS_TURMAS;
    }

    /**
     * Inicializa a página de cadastro de uma turma.
     *
     * @return
     */
    public String iniciarPaginaCadastrar() {
        limparPaginaCadastro();
        buscarAlunos();
        return GO_CADASTRAR_TURMA;
    }

    /**
     * Inicializa a página de detalhe de uma turma.
     *
     * @param turmaSelecionada
     * @return
     */
    public String iniciarPaginaDetalhar(Turma turmaSelecionada) {
        selecionarTurma(turmaSelecionada);
        return GO_DETALHAR_TURMA;
    }
    
    private void limparPaginaCadastro(){
        turma = new Turma();
        alunosSelecionados.clear();
        todosSelecionados = false;
    }

    /**
     * Lista todas as turmas de um professor.
     */
    private void buscarTurmas() {
        turmas = turmaServico.buscarTurmas(usuarioLogado);
    }
    
    /**
     * Lista todos os alunos cadastrados.
     */
    private void buscarAlunos(){
        alunos = usuarioServico.buscarAlunos();
    }
    
    public void selecionarTodos(){
        alunosSelecionados.clear();
        
        if(todosSelecionados){
            alunosSelecionados.addAll(alunos);
        }
        
        for (Usuario aluno : alunos) {
            aluno.setSelecionado(todosSelecionados);
        }
    }
    
    public void selecionarAluno(Usuario aluno){
        if(alunosSelecionados.contains(aluno)){
            alunosSelecionados.remove(aluno);
        }else{
            alunosSelecionados.add(aluno);
        }
    }

    /**
     * Cadastrar uma nova turma.
     * 
     * @return 
     */
    public String cadastrarTurma() {
        String navegacao = "";
        try {
            turma.setProfessor(usuarioLogado);
            turma.setAlunos(alunosSelecionados);
            turmaServico.salvar(turma);
            navegacao = iniciarPagina();
        } catch (ValidacaoException ex) {
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(null, mensagem);
        }

        return navegacao;
    }
    
    /**
     * Seleciona uma turma para detalhe ou exclusão.
     * 
     * @param turmaSelecionada 
     */
    private void selecionarTurma(Turma turmaSelecionada) {
        turma = turmaSelecionada;
    }
    
    public void abrirModalExcluir(Turma turmaSelecionada) {
        selecionarTurma(turmaSelecionada);
        exibirModalExcluir = true;
    }
    
    /**
     * Excluir uma turma selecionada.
     */
    public void excluirTurma(){
        turmaServico.remover(turma);
        fecharModalExcluir();
    }
    
    /**
     * Fecha o modal de exclusão.
     */
    public void fecharModalExcluir() {
        exibirModalExcluir = false;
    }

    public List<Turma> getTurmas() {
        return turmas;
    }

    public List<Usuario> getAlunos() {
        return alunos;
    }
    
    public Turma getTurma() {
        return turma;
    }

    public boolean isExibirModalExcluir() {
        return exibirModalExcluir;
    }

    public List<Usuario> getAlunosSelecionados() {
        return alunosSelecionados;
    }

    public void setTodosSelecionados(boolean todosSelecionados) {
        this.todosSelecionados = todosSelecionados;
    }

    public boolean isTodosSelecionados() {
        return todosSelecionados;
    }
    
}
