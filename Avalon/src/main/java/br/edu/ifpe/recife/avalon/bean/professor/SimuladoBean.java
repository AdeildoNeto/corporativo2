/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean.professor;

import br.edu.ifpe.recife.avalon.bean.comum.AbstractSimuladoBean;
import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.avaliacao.simulado.Simulado;
import br.edu.ifpe.recife.avalon.model.avaliacao.simulado.SimuladoAluno;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;

/**
 *
 * @author eduardoamaral
 */
@Named(value = SimuladoBean.NOME)
@SessionScoped
public class SimuladoBean extends AbstractSimuladoBean {

    public static final String NOME = "simuladoBean";
    private static final String GO_GERAR_SIMULADO = "goGerarSimulado";
    private static final String GO_LISTAR_SIMULADO = "goLisarSimulado";
    private static final String GO_VISUALIZAR_SIMULADO = "goVisualizarSimulado";
    private static final String GO_SIMULADO_RESULTADOS = "goResultadosSimulado";
    private static final String GO_DETALHAR_RESULTADO = "goDetalharResultadoSimulado";
    private static final String USUARIO = "usuario";

    /**
     * Cria uma nova instância de <code>SimuladoBean</code>.
     */
    public SimuladoBean() {
        usuarioLogado = (Usuario) sessao.getAttribute(USUARIO);
        simulado = new Simulado();
    }

    /**
     * Inicializa os dados necessários para a página de geração de prova.
     *
     * @return rota para página de geração de prova
     */
    public String iniciarPagina() {
        limparPagina();
        super.limparPagina();
        buscarSimulados();

        return GO_LISTAR_SIMULADO;
    }

    /**
     * Inicializa os dados necessários para p[agina gerar novo simulado.
     *
     * @return navegação.
     */
    public String iniciarPaginaGerar() {
        inicializarPaginaGerar();
        return GO_GERAR_SIMULADO;

    }

    /**
     * Inicializa a página de resultados de um simulado.
     *
     * @param simulado
     * @return
     */
    public String iniciarPaginaResultados(Simulado simulado) {
        inicializarPaginaResultados(simulado);
        return GO_SIMULADO_RESULTADOS;
    }

    /**
     * Inicializa os dados necessários para a página visualizar simulado.
     *
     * @param simuladoSelecionado - simulado selecionado.
     * @return navegacao
     */
    public String iniciarPaginaVisualizarSimulado(Simulado simuladoSelecionado) {
        inicializarPaginaVisualizarSimulado(simuladoSelecionado);
        return GO_VISUALIZAR_SIMULADO;
    }

    /**
     * Inicializa a página de detalhes de um resultado.
     *
     * @param simuladoAluno
     * @return rota
     */
    public String iniciarPaginaDetalhar(SimuladoAluno simuladoAluno) {
        inicializarPaginaDetalhar(simuladoAluno);
        return GO_DETALHAR_RESULTADO;
    }

    /**
     * Carrega os simulados do usuário.
     */
    private void buscarSimulados() {
        simulados = simuladoServico.buscarSimuladosUsuario(usuarioLogado);
    }

    /**
     * Salva um novo simulado.
     *
     * @return navegacao
     */
    public String salvar() {
        String nav = super.salvarSimulado(GO_LISTAR_SIMULADO);
        
        if(nav != null){
            iniciarPagina();
        }
        
        return nav;
    }

}
