/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.excecao;

/**
 *
 * @author eduardoamaral
 */
public class ValidacaoException extends Exception {

    /**
     * Cria uma nova instância de <code>ValidacaoException</code> sem mensagem de detalhes.
     */
    public ValidacaoException() {
    }

    /**
     * Cria uma nova instância de <code>ValidacaoException</code> com mensagem de detalhes
     * específica.
     * 
     * @param msg a mensagem de detalhe.
     */
    public ValidacaoException(String msg) {
        super(msg);
    }
}
