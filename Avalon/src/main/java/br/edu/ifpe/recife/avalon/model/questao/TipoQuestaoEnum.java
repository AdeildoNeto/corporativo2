/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.questao;

/**
 *
 * @author eduardo.f.amaral
 */
public enum TipoQuestaoEnum {
    
    DISCURSIVA(1, "Discursiva"),
    MULTIPLA_ESCOLHA(2, "Multipla Escolha"),
    VERDADEIRO_FALSO(3, "Verdadeiro ou Falso");
    
    public int codigo;
    public String descricao;

    private TipoQuestaoEnum(int codigo, String descricao) {
        this.descricao = descricao;
    }
    
}
