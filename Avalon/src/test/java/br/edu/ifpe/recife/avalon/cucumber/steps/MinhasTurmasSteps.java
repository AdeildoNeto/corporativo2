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
public class MinhasTurmasSteps {
    
    public MinhasTurmasSteps() {
        DbUnitUtil.setDataSet(DataSetEnum.PROVAS_SIMULADOS);
        DbUnitUtil.inserirDados();
    }
    
    @E("^esteja na página minhas turmas$")
    public void irParaMinhasTurmas() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("menu:menuTurmas")).click();
        BrowserManager.waitTime(3000);
    }
        
}
