/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.viewhelper;

import br.edu.ifpe.recife.avalon.model.questao.FiltroQuestao;
import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.TipoQuestaoEnum;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.servico.QuestaoServico;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author eduardoamaral
 */
public class PesquisarQuestaoViewHelper implements Serializable {
    
    private QuestaoServico questaoServico;
    
    private FiltroQuestao filtro = new FiltroQuestao();
    private List<TipoQuestaoEnum> tipoQuestoes = new ArrayList<>();
    private Usuario usuarioLogado;
    private boolean filtroSimulado;
    
    /**
     * Inicializa a instância de <code>PesquisarQuestaoViewHelper</code>.
     * @param servico - QuestaoServico
     * @param usuario - Usuario
     */
    public void inicializar(QuestaoServico servico, Usuario usuario, boolean filtroSimulado){
        this.filtro = new FiltroQuestao();
        this.usuarioLogado = usuario;
        this.questaoServico = servico;
        this.filtroSimulado = filtroSimulado;
        carregarTiposQuestao();
    }
    
    /**
     * Carrega os tipos de questão disponíveis.
     */
    private void carregarTiposQuestao() {
        if(filtroSimulado){
            this.tipoQuestoes = Arrays.asList(TipoQuestaoEnum.MULTIPLA_ESCOLHA, TipoQuestaoEnum.VERDADEIRO_FALSO);
        }else{
            this.tipoQuestoes = Arrays.asList(TipoQuestaoEnum.values());
        }
    }
    
    /**
     * Carrega as questões a partir do filtro informado.
     * @return lista de questoes.
     */
    public List<Questao> pesquisar() {
        this.filtro.setEmailUsuario(usuarioLogado.getEmail());
        
        return questaoServico.buscarQuestoesPorFiltro(filtro);
    }

    public FiltroQuestao getFiltro() {
        return filtro;
    }

    public List<TipoQuestaoEnum> getTipoQuestoes() {
        return tipoQuestoes;
    }

    public boolean isFiltroSimulado() {
        return filtroSimulado;
    }

    public void setFiltroSimulado(boolean filtroSimulado) {
        this.filtroSimulado = filtroSimulado;
    }
    
}
