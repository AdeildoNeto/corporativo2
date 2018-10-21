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
public class DetalharSimuladoSteps {

    @Quando("^o professor clicar no botão visualizar simulado em um simulado de verdadeiro ou falso$")
    public void detalharSimuladoVF() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:tableSimulados:0:btnDetalharSimulado")).click();
        BrowserManager.waitTime(2000);
    }
    
    @Entao("^será exibido os detalhes do simulado de verdadeiro ou falso$")
    public void exibirDetalheSimuladoVF() throws Throwable {
        int resultados = BrowserManager.getDriver().findElements(By.xpath("//span[contains(text(), 'Verdadeiro')]")).size();
        assertTrue(resultados > 0);
        LoginSteps.logout();
    }
    
    @Quando("^o professor clicar no botão visualizar simulado em um simulado de múltipla escolha$")
    public void detalharSimuladoMultiplaEscolha() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:tableSimulados:1:btnDetalharSimulado")).click();
        BrowserManager.waitTime(2000);
    }
    
    @Entao("^será exibido os detalhes do simulado de múltipla escolha$")
    public void exibirDetalheSimuladoMultiplaEscolha() throws Throwable {
        int resultados = BrowserManager.getDriver().findElements(By.xpath("//div[contains(text(), '1)')]")).size();
        assertTrue(resultados > 0);
        LoginSteps.logout();
    }
    
}
