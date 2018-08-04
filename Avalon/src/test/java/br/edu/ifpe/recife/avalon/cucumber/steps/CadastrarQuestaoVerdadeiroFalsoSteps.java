/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.cucumber.steps;

import br.edu.ifpe.recife.avalon.cucumber.util.BrowserManager;
import cucumber.api.java.pt.Quando;
import org.openqa.selenium.By;

/**
 *
 * @author eduardoamaral
 */
public class CadastrarQuestaoVerdadeiroFalsoSteps {

    @Quando("^o professor selecionar o tipo verdadeiro ou falso")
    public void selecionarTipoVerdadeiroFalso() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:selTipo_label")).click();
        BrowserManager.getDriver().findElement(By.xpath("//*[@data-label='Verdadeiro ou Falso']")).click();
    }
        
}
