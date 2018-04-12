/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.bean;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author eduardo.f.amaral
 */
public class BaseBean implements Serializable {
    
    private ResourceBundle messageBundle;
    private ResourceBundle validationBundle;
    

    public BaseBean() {
       messageBundle = ResourceBundle.getBundle("mensagens.mensagem", new Locale("pt", "BR"));
       validationBundle = ResourceBundle.getBundle("questao.enunciado.repetido", new Locale("pt", "BR"));
    }

    protected String getMensagem(String chave){
        if(chave == null){
            return "";
        }
        
        return messageBundle.getString(chave);
    }
    
    protected String getMensagemValidacao(String chave){
        if(chave == null){
            return "";
        }
        
        return validationBundle.getString(chave);
    }
            
}