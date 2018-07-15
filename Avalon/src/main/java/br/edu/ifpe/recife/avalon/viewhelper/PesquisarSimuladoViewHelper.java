/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.viewhelper;

import br.edu.ifpe.recife.avalon.model.simulado.FiltroSimulado;
import br.edu.ifpe.recife.avalon.model.simulado.Simulado;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.servico.SimuladoServico;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author eduardoamaral
 */
public class PesquisarSimuladoViewHelper implements Serializable {
    
    private SimuladoServico simuladoServico;
    
    private FiltroSimulado filtro = new FiltroSimulado();
    private Usuario usuarioLogado;
    
    public void inicializar(SimuladoServico servico, Usuario usuario){
        this.filtro = new FiltroSimulado();
        this.usuarioLogado = usuario;
        this.simuladoServico = servico;
    }
    
    public void limparFiltro(){
        this.filtro = new FiltroSimulado();
    }
    
    /**
     * Método para carregar as questões do usuário.
     * @return lista de questoes.
     */
    public List<Simulado> pesquisar() {
        return simuladoServico.buscarSimuladoPorFiltro(filtro);
    }

    public FiltroSimulado getFiltro() {
        return filtro;
    }

}
