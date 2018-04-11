/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean;

import br.edu.ifpe.recife.avalon.model.questao.Questao;
import br.edu.ifpe.recife.avalon.model.questao.TipoQuestaoEnum;
import br.edu.ifpe.recife.avalon.servico.QuestaoServico;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.validation.Valid;

/**
 *
 * @author eduardo.f.amaral
 */
@Named(value = "questaoBean")
@SessionScoped
public class QuestaoBean implements Serializable {

    @EJB
    private QuestaoServico questaoServico;
    
    private List<TipoQuestaoEnum> tipoQuestoes = new ArrayList<>();
    
    private TipoQuestaoEnum tipoSelecionado = TipoQuestaoEnum.DISCURSIVA;
    
    @Valid
    private Questao novaQuestao;
    
    public QuestaoBean() {
        this.novaQuestao = new Questao();
        this.carregarTiposQuestao();
    }
    
    private void carregarTiposQuestao(){
        this.tipoQuestoes.add(TipoQuestaoEnum.DISCURSIVA);
        this.tipoQuestoes.add(TipoQuestaoEnum.MULTIPLA_ESCOLHA);
        this.tipoQuestoes.add(TipoQuestaoEnum.VERDADEIRO_FALSO);
    }
    
    public String salvar(){
        questaoServico.salvar(novaQuestao);
        
        return "";
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

    public Questao getNovaQuestao() {
        return novaQuestao;
    }

    public void setNovaQuestao(Questao novaQuestao) {
        this.novaQuestao = novaQuestao;
    }

    public TipoQuestaoEnum getTipoSelecionado() {
        return tipoSelecionado;
    }

    public void setTipoSelecionado(TipoQuestaoEnum tipoSelecionado) {
        this.tipoSelecionado = tipoSelecionado;
    }
    
}
