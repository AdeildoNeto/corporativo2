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
public class ProvasDisponiveisSteps {

    public ProvasDisponiveisSteps() {
        DbUnitUtil.setDataSet(DataSetEnum.REALIZAR_PROVA);
        DbUnitUtil.inserirDados();
    }
    
    @E("^esteja na página de provas disponíveis$")
    public void irParaProvasDisponiveis() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("menu:menuProvas")).click();
        BrowserManager.waitTime(3000);
    }
    
}
