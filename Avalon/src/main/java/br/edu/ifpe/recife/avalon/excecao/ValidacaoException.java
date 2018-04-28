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
     * Creates a new instance of <code>NegocioException</code> without detail
     * message.
     */
    public ValidacaoException() {
    }

    /**
     * Constructs an instance of <code>NegocioException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ValidacaoException(String msg) {
        super(msg);
    }
}
