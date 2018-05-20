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
    private boolean exibirOpcaoTipo = true;
    
    public void inicializar(QuestaoServico servico, Usuario usuario){
        this.filtro = new FiltroQuestao();
        this.usuarioLogado = usuario;
        this.questaoServico = servico;
        carregarTiposQuestao();
    }
    
    /**
     * Método responsável por carregar os tipos de questão disponíveis.
     */
    private void carregarTiposQuestao() {
        this.tipoQuestoes = Arrays.asList(TipoQuestaoEnum.values());        
    }
    
    /**
     * Método para carregar as questões do usuário.
     * @return lista de questoes.
     */
    public List<Questao> pesquisar() {
        this.filtro.setEmailUsuario(usuarioLogado.getEmail());
        
        if(exibirOpcaoTipo){
            return questaoServico.buscarQuestoesPorFiltro(filtro);
        }else{
            return questaoServico.buscarQuestoesParaSimulado(filtro);
        }
    }

    public FiltroQuestao getFiltro() {
        return filtro;
    }

    public List<TipoQuestaoEnum> getTipoQuestoes() {
        return tipoQuestoes;
    }

    public boolean isExibirOpcaoTipo() {
        return exibirOpcaoTipo;
    }

    public void setExibirOpcaoTipo(boolean exibirOpcaoTipo) {
        this.exibirOpcaoTipo = exibirOpcaoTipo;
    }
    
}
