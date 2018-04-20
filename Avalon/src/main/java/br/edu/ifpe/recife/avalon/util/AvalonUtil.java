/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author eduardoamaral
 */
public class AvalonUtil {

    private static final AvalonUtil INSTANCE = new AvalonUtil();
    private final ResourceBundle messageBundle;
    private final ResourceBundle validationBundle;
    
    private AvalonUtil() {
        messageBundle = ResourceBundle.getBundle("mensagens.mensagem", new Locale("pt", "BR"));
        validationBundle = ResourceBundle.getBundle("ValidationMessages", new Locale("pt", "BR"));
    }
    
    public static AvalonUtil getInstance(){
        return INSTANCE;
    }
    
    /**
     * Método responsável por recuperar uma mensagem internacionalizada.
     * @param chave
     * @return mensagem
     */
    public String getMensagem(String chave){
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
    public String getMensagemValidacao(String chave){
        if(chave == null){
            return "";
        }
        
        return validationBundle.getString(chave);
    }
    
    public static String quebrarLinha(){
        return System.getProperty("line.separator");
    }
    
    public static String formatarEnunciado(String enunciado){
        
        if(enunciado == null){
            return "";
        }
        
        return enunciado.replaceAll("\r\n", quebrarLinha());
        
    }
    
}
