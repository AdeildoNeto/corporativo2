/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.viewhelper;

import br.edu.ifpe.recife.avalon.model.questao.MultiplaEscolha;
import br.edu.ifpe.recife.avalon.model.questao.VerdadeiroFalso;
import br.edu.ifpe.recife.avalon.model.simulado.Simulado;
import br.edu.ifpe.recife.avalon.servico.QuestaoServico;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author eduardoamaral
 */
public class VisualizarAvaliacaoViewHelper implements Serializable {

    private QuestaoServico questaoServico;

    private Simulado simuladoSelecionado;
    private List<VerdadeiroFalso> questoesVerdadeiroFalso = new ArrayList<>();
    private List<MultiplaEscolha> questoesMultiplaEscolha = new ArrayList<>();

    public void inicializar(QuestaoServico servico) {
        questaoServico = servico;
    }
    
    public void limpar(){
        questoesMultiplaEscolha.clear();
        questoesVerdadeiroFalso.clear();
    }

    public void setQuestoesMultiplaEscolha(List<MultiplaEscolha> questoesMultiplaEscolha) {
        limpar();
        this.questoesMultiplaEscolha = questoesMultiplaEscolha;
    }

    public List<MultiplaEscolha> getQuestoesMultiplaEscolha() {
        return questoesMultiplaEscolha;
    }

    public void setQuestoesVerdadeiroFalso(List<VerdadeiroFalso> questoesVerdadeiroFalso) {
        limpar();
        this.questoesVerdadeiroFalso = questoesVerdadeiroFalso;
    }
    
    public List<VerdadeiroFalso> getQuestoesVerdadeiroFalso() {
        return questoesVerdadeiroFalso;
    }

    public Simulado getSimuladoSelecionado() {
        return simuladoSelecionado;
    }

}
