/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.recife.avalon.cucumber.steps;

import br.edu.ifpe.recife.avalon.cucumber.util.BrowserManager;
import cucumber.api.java.pt.E;
import cucumber.api.java.pt.Entao;
import cucumber.api.java.pt.Quando;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.By;

/**
 *
 * @author eduardoamaral
 */
public class VisualizarResultadoProvaSteps {
    
    @Quando("^o aluno selecionar a prova que deseja conhecer o resultado$")
    public void detalharResultadoProva() throws Throwable {
        BrowserManager.getDriver().findElement(By.id("form:table:0:btnDetalharResultado")).click();
        BrowserManager.waitTime(2000);
    }
    
    @Entao("^será exibido o resultado da prova")
    public void exibirResultadoProva() throws Throwable {
        int resultados = BrowserManager.getDriver().findElements(By.xpath("//span[contains(text(), '1)')]")).size();
        assertTrue(resultados > 0);
        LoginSteps.logout();
    }
    
}
