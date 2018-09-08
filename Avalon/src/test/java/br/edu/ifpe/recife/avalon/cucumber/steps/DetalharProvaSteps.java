/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.cucumber.steps;

import br.edu.ifpe.recife.avalon.cucumber.util.BrowserManager;
import cucumber.api.java.pt.Entao;
import cucumber.api.java.pt.Quando;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.By;

/**
 *
 * @author eduardoamaral
 */
public class DetalharProvaSteps {

    @Quando("^o professor clicar no botão visualizar prova em uma prova de verdadeiro ou falso$")
    public void detalharProvaVF() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:tableProvas:0:btnDetalharProva")).click();
        BrowserManager.waitTime(2000);
    }
    
    @Entao("^será exibido os detalhes da prova de verdadeiro ou falso$")
    public void exiberDetalheProvaVF() throws Throwable {
        int resultados = BrowserManager.getDriver().findElements(By.xpath("//span[contains(text(), 'Verdadeiro')]")).size();
        assertTrue(resultados > 0);
        LoginSteps.logout();
    }
    
    @Quando("^o professor clicar no botão visualizar prova em uma prova de múltipla escolha$")
    public void detalharProvaMultiplaEscolha() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:tableProvas:1:btnDetalharProva")).click();
        BrowserManager.waitTime(2000);
    }
    
    @Entao("^será exibido os detalhes da prova de múltipla escolha$")
    public void exibirDetalheMultiplaEscolha() throws Throwable {
        int resultados = BrowserManager.getDriver().findElements(By.xpath("//div[contains(text(), '1)')]")).size();
        assertTrue(resultados > 0);
        LoginSteps.logout();
    }
    
}
