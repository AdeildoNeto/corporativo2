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
       validationBundle = ResourceBundle.getBundle("ValidationMessages", new Locale("pt", "BR"));
    }

    /**
     * Método responsável por recuperar uma mensagem internacionalizada.
     * @param chave
     * @return mensagem
     */
    protected String getMensagem(String chave){
        if(chave == null){
            return "";
        }
        
        return messageBundle.getString(chave);
    }
    
    /**
     * Método responsável por recuperar uma mensagem de validação internacionalizada.
     * @param chave
     * @return mensagem de validação
     */
    protected String getMensagemValidacao(String chave){
        if(chave == null){
            return "";
        }
        
        return validationBundle.getString(chave);
    }
            
}