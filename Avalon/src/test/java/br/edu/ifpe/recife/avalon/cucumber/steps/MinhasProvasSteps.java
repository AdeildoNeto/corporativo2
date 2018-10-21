/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.cucumber.steps;

import br.edu.ifpe.recife.avalon.cucumber.util.BrowserManager;
import br.edu.ifpe.recife.avalon.cucumber.util.DataSetEnum;
import br.edu.ifpe.recife.avalon.cucumber.util.DbUnitUtil;
import cucumber.api.java.pt.E;
import org.openqa.selenium.By;

/**
 *
 * @author eduardoamaral
 */
public class MinhasProvasSteps {

    public MinhasProvasSteps() {
        DbUnitUtil.setDataSet(DataSetEnum.MINHAS_PROVAS);
        DbUnitUtil.inserirDados();
    }

    @E("^esteja na página de provas$")
    public void irParaMinhasProvas() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("menu:menuProvas")).click();
        BrowserManager.waitTime(3000);
    }

    @E("^deseje imprimir uma prova$")
    public void irParaImprimirProva() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:btnImprimirProva")).click();
        BrowserManager.waitTime(3000);
    }

    @E("^deseje cadastrar uma nova prova online$")
    public void irParaGerarProva() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:btnGerarProva")).click();
        BrowserManager.waitTime(3000);
    }

}
