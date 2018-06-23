/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean.professor;

import br.edu.ifpe.recife.avalon.model.prova.Prova;
import br.edu.ifpe.recife.avalon.model.prova.ProvaAluno;
import br.edu.ifpe.recife.avalon.servico.ProvaServico;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author eduardoamaral
 */

@Named(value = ResultadoBean.RESULTADO_BEAN)
@SessionScoped
public class ResultadoBean implements Serializable{
    public static final String RESULTADO_BEAN = "resultadoBean";
    
    private static final String GO_PROVA_RESULTADOS = "goProvaResultados";
    
    @EJB
    private ProvaServico provaServico;
    
    private List<ProvaAluno> resultados;
    private ProvaAluno provaAlunoDetalhe;
    private Prova provaSelecionada;

    public ResultadoBean() {
    }
    
    public String iniciarPaginaResultados(Prova prova){
        provaSelecionada = prova;
        buscarResultados(prova);
        
        return GO_PROVA_RESULTADOS;
    }
    
    private void buscarResultados(Prova prova){
        resultados = provaServico.buscarResultadosProva(prova);
    }

    public ProvaAluno getProvaAlunoDetalhe() {
        return provaAlunoDetalhe;
    }

    public Prova getProvaSelecionada() {
        return provaSelecionada;
    }

    public List<ProvaAluno> getResultados() {
        return resultados;
    }
    
}
