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
 * @author eduardo.f.amaral
 */
public class PesquisarQuestaoViewHelper implements Serializable {
    
    public static final String NOME = "pesquisarQuestaoViewHelper";
    
    private QuestaoServico questaoServico;
    
    private FiltroQuestao filtro = new FiltroQuestao();
    private List<TipoQuestaoEnum> tipoQuestoes = new ArrayList<>();
    private Usuario usuarioLogado;
    
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
        return questaoServico.buscarQuestoesPorFiltro(filtro);
    }

    public FiltroQuestao getFiltro() {
        return filtro;
    }

    public List<TipoQuestaoEnum> getTipoQuestoes() {
        return tipoQuestoes;
    }
    
}
