/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.cucumber.util;

/**
 *
 * @author eduardoamaral
 */
public enum DataSetEnum {
    
    CADASTRAR_QUESTOES("src/main/resources/dbunit/cadastrarQuestoes.xml"),
    MINHAS_PROVAS("src/main/resources/dbunit/minhasProvas.xml"),
    REALIZAR_PROVA("src/main/resources/dbunit/realizarProva.xml");

    private final String source;
    
    private DataSetEnum(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }
    
}
