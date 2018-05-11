/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.usuario;

public enum GrupoEnum {

    PROFESSOR(1, "Professor"),
    ALUNO(2, "Aluno");
    
    private final int codigo;
    private final String descricao;
    
    private GrupoEnum(int codigo, String descricao){
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }
    
}
