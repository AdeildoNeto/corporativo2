/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.questao.enums;

/**
 *
 * @author eduardoamaral
 */
public enum TipoQuestaoEnum {
    
    DISCURSIVA(1, "Discursiva", "Discursiva"),
    MULTIPLA_ESCOLHA(2, "Múltipla Escolha", "M. escolha"),
    VERDADEIRO_FALSO(3, "Verdadeiro ou Falso", "V/F");
    
    private final int codigo;
    private final String descricao;
    private final String abreviacao;

    private TipoQuestaoEnum(int codigo, String descricao, String abreviacao) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.abreviacao = abreviacao;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getAbreviacao() {
        return abreviacao;
    }
    
}
