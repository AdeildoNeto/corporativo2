/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.viewhelper;

import br.edu.ifpe.recife.avalon.excecao.ValidacaoException;
import br.edu.ifpe.recife.avalon.model.questao.MultiplaEscolha;
import br.edu.ifpe.recife.avalon.model.questao.VerdadeiroFalso;
import br.edu.ifpe.recife.avalon.model.simulado.Simulado;
import br.edu.ifpe.recife.avalon.servico.QuestaoServico;
import br.edu.ifpe.recife.avalon.util.AvalonUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author eduardoamaral
 */
public class VisualizarSimuladoViewHelper implements Serializable {

    private QuestaoServico questaoServico;

    private Simulado simuladoSelecionado;
    private List<VerdadeiroFalso> questoesVerdadeiroFalso = new ArrayList<>();
    private List<MultiplaEscolha> questoesMultiplaEscolha = new ArrayList<>();

    public void inicializar(QuestaoServico servico) {
        questaoServico = servico;
    }

    public void carregarQuestoes(Simulado simuladoSelecionado) throws ValidacaoException {
        limpar();
        
        if (simuladoSelecionado.getQuestoes().get(0) instanceof VerdadeiroFalso) {
            questoesVerdadeiroFalso = (List<VerdadeiroFalso>) (List<?>) questaoServico.buscarQuestoesPorSimulado(simuladoSelecionado.getId());
        } else {
            questoesMultiplaEscolha = (List<MultiplaEscolha>) (List<?>) questaoServico.buscarQuestoesPorSimulado(simuladoSelecionado.getId());
        }

        if (questoesVerdadeiroFalso.isEmpty() && questoesMultiplaEscolha.isEmpty()) {
            throw new ValidacaoException(AvalonUtil.getInstance().getMensagem("simulado.vazio"));
        }
    }
    
    public void limpar(){
        questoesMultiplaEscolha.clear();
        questoesVerdadeiroFalso.clear();
    }

    public List<MultiplaEscolha> getQuestoesMultiplaEscolha() {
        return questoesMultiplaEscolha;
    }

    public List<VerdadeiroFalso> getQuestoesVerdadeiroFalso() {
        return questoesVerdadeiroFalso;
    }

    public Simulado getSimuladoSelecionado() {
        return simuladoSelecionado;
    }

}
