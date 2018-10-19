/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.filtro;

import br.edu.ifpe.recife.avalon.model.questao.enums.TipoQuestaoEnum;

/**
 *
 * @author eduardoamaral
 */
public class FiltroQuestao {
    
    private Long idComponenteCurricular;
    private String enunciado = "";
    private String nomeProfessor = "";
    private String emailUsuario = "";
    private Boolean questaoSimulado;
    private boolean apenasQuestoesObjetivas = false;
    private TipoQuestaoEnum tipo = TipoQuestaoEnum.DISCURSIVA;

    public FiltroQuestao() {
        super();
    }
    
    public FiltroQuestao(String emailUsuario, boolean apenasQuestoesObjetivas){
        this.emailUsuario = emailUsuario;
        this.apenasQuestoesObjetivas = apenasQuestoesObjetivas;
    }

    public String getNomeProfessor() {
        return nomeProfessor;
    }

    public void setNomeProfessor(String nomeProfessor) {
        this.nomeProfessor = nomeProfessor;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public Long getIdComponenteCurricular() {
        return idComponenteCurricular;
    }

    public void setIdComponenteCurricular(Long idComponenteCurricular) {
        this.idComponenteCurricular = idComponenteCurricular;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public TipoQuestaoEnum getTipo() {
        return tipo;
    }

    public void setTipo(TipoQuestaoEnum tipo) {
        this.tipo = tipo;
    }

    public Boolean getQuestaoSimulado() {
        return questaoSimulado;
    }

    public void setQuestaoSimulado(Boolean questaoSimulado) {
        this.questaoSimulado = questaoSimulado;
    }

    public boolean isApenasQuestoesObjetivas() {
        return apenasQuestoesObjetivas;
    }

    public void setApenasQuestoesObjetivas(boolean apenasQuestoesObjetivas) {
        this.apenasQuestoesObjetivas = apenasQuestoesObjetivas;
    }

}
