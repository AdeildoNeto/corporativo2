/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.model.filtro;

import java.io.Serializable;

/**
 *
 * @author eduardoamaral
 */
public class FiltroSimulado implements Serializable {
    
    private Long idComponenteCurricular;
    private String titulo = "";
    private String nomeProfessor = "";

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

    public String getnomeProfessor() {
        return nomeProfessor;
    }

    public void setnomeProfessor(String nomeProfessor) {
        this.nomeProfessor = nomeProfessor;
    }

}
