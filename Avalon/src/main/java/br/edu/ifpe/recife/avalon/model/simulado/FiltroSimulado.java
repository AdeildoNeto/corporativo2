/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.simulado;

import java.io.Serializable;

/**
 *
 * @author eduardoamaral
 */
public class FiltroSimulado implements Serializable {
    
    private Long idComponenteCurricular;
    private String titulo = "";
    private String nomeCriador = "";

    public Long getIdComponenteCurricular() {
        return idComponenteCurricular;
    }

    public void setIdComponenteCurricular(Long idComponenteCurricular) {
        this.idComponenteCurricular = idComponenteCurricular;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNomeCriador() {
        return nomeCriador;
    }

    public void setNomeCriador(String nomeCriador) {
        this.nomeCriador = nomeCriador;
    }

}
