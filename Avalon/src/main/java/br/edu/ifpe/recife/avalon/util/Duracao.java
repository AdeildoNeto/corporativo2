/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.util;

/**
 *
 * @author aneto
 */
public class Duracao {
    
    private long minutos;
    
    private long segundos;

    
    public Duracao(){
        
    }
    /**
     * @return the minutos
     */
    public long getMinutos() {
        return minutos;
    }

    /**
     * @param minutos the minutos to set
     */
    public void setMinutos(long minutos) {
        this.minutos = minutos;
    }

    /**
     * @return the segundos
     */
    public long getSegundos() {
        return segundos;
    }

    /**
     * @param segundos the segundos to set
     */
    public void setSegundos(long segundos) {
        this.segundos = segundos;
    }
    
}
