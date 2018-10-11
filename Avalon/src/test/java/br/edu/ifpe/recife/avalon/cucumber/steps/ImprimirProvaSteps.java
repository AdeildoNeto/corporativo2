/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.cucumber.steps;

import br.edu.ifpe.recife.avalon.cucumber.util.BrowserManager;
import cucumber.api.java.pt.E;
import cucumber.api.java.pt.Entao;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.By;

/**
 *
 * @author eduardoamaral
 */
public class ImprimirProvaSteps {
    
    private String abaPrincipal;

    @E("^selecionar uma questão$")
    public void selecionarQuestao() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:table:0:chkSelecionarQuestao")).click();
        BrowserManager.waitTime(1000);
    }

    @E("^clicar no botão imprimir$")
    public void imprimirProva() throws Throwable {
        abaPrincipal = BrowserManager.getDriver().getWindowHandle();
        BrowserManager.getDriver().findElement(By.id("form:btn")).click();
        BrowserManager.waitTime(1000);
    }
    
    @Entao("^será gerado o pdf da prova com as questões selecionadas$")
    public void imprimirProvaMultiplaEscolha() throws Throwable {
        BrowserManager.waitTime(1000);
        LoginSteps.logout();
    }
    
}
