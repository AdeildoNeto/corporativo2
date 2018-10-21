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
public class MeusSimuladosSteps {

    public MeusSimuladosSteps() {
        DbUnitUtil.setDataSet(DataSetEnum.MEUS_SIMULADOS);
        DbUnitUtil.inserirDados();
    }
    
    @E("^esteja na página de simulados$")
    public void irParaMinhasProvas() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("menu:menuSimulados")).click();
        BrowserManager.waitTime(3000);
    }

    @E("^deseje cadastrar um novo simulado$")
    public void irParaImprimirProva() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:btnGerarSimulado")).click();
        BrowserManager.waitTime(3000);
    }
    
    @E("^esteja na página de simulados disponíveis$")
    public void irParaSimuladosDisponiveis() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("menu:menuSimulados")).click();
        BrowserManager.waitTime(3000);
    }
    
}
