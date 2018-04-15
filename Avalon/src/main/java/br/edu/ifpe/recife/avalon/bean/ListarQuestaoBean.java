/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean;

import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.TipoQuestaoEnum;
import br.edu.ifpe.recife.avalon.model.usuario.Usuario;
import br.edu.ifpe.recife.avalon.servico.QuestaoServico;
import br.edu.ifpe.recife.avalon.servico.UsuarioServico;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;

/**
 *
 * @author eduardo.f.amaral
 */
@Named(value = "listarQuestaoBean")
@SessionScoped
public class ListarQuestaoBean extends BaseBean implements Serializable{

    @EJB
    private QuestaoServico questaoServico;
    
    @EJB
    private UsuarioServico usuarioServico;
    
    private List<TipoQuestaoEnum> tipoQuestoes = new ArrayList<>();
    
    private TipoQuestaoEnum tipoSelecionado = TipoQuestaoEnum.DISCURSIVA;
    
    private List<Questao> questoes = new ArrayList<>();
    
    
    public ListarQuestaoBean() {
        this.carregarTiposQuestao();
    }
    
    @PostConstruct
    private void init(){
        this.carregarQuestoes();
    }
    
    /**
     * Método responsável por carregar os tipos de questão disponíveis.
     */
    private void carregarTiposQuestao(){
        this.tipoQuestoes.add(TipoQuestaoEnum.DISCURSIVA);
        this.tipoQuestoes.add(TipoQuestaoEnum.MULTIPLA_ESCOLHA);
        this.tipoQuestoes.add(TipoQuestaoEnum.VERDADEIRO_FALSO);
    }
    
    private void carregarQuestoes(){
        Usuario criador = new Usuario();
        criador.setId(1l);
        this.questoes = questaoServico.buscarQuestoesPorCriador(criador);
    }
    
    public QuestaoServico getQuestaoServico() {
        return questaoServico;
    }

    public void setQuestaoServico(QuestaoServico questaoServico) {
        this.questaoServico = questaoServico;
    }

    public List<TipoQuestaoEnum> getTipoQuestoes() {
        return tipoQuestoes;
    }

    public void setTipoQuestoes(List<TipoQuestaoEnum> tipoQuestoes) {
        this.tipoQuestoes = tipoQuestoes;
    }

    public TipoQuestaoEnum getTipoSelecionado() {
        return tipoSelecionado;
    }

    public void setTipoSelecionado(TipoQuestaoEnum tipoSelecionado) {
        this.tipoSelecionado = tipoSelecionado;
    }

    public List<Questao> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(List<Questao> questoes) {
        this.questoes = questoes;
    }
    
}
